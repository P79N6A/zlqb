package com.nyd.capital.service.cache.map;

import com.nyd.capital.service.cache.Cache;
import com.nyd.capital.service.cache.config.CacheConfig;
import com.nyd.capital.service.cache.model.CacheKey;
import com.nyd.capital.service.cache.model.CacheWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Map实现本地缓存
 * Cong Yuxiang
 * 2017/11/21
 */
public class MapCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(MapCache.class);

    /** 缓存配置类 */
    private final CacheConfig config;

    private final MapCacheDaemon                    daemon;

    private final ConcurrentHashMap<String, Object> cache;


    private Thread                                  daemonThread;

    public MapCache() {
        this(new CacheConfig());
    }

    public MapCache(CacheConfig config) {
        logger.info("Map Cache initing...");

        this.config = config;
        cache = new ConcurrentHashMap<String, Object>();
        daemon = new MapCacheDaemon(this, config);

        //读取磁盘缓存
        daemon.readCacheFromDisk();

        //启动守护线程
        startDaemon();


        logger.info("Map Cache init success!");
    }

    @Override
    public void set(CacheKey cacheKey, CacheWrapper wrapper) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey()) || wrapper == null) {
            return;
        }
        cache.put(key, wrapper);


    }

    @Override
    public CacheWrapper get(CacheKey cacheKey) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return null;
        }

        CacheWrapper wrapper = null;
        try {
            Object value = cache.get(key);
            if (value != null) {
                wrapper = (CacheWrapper) value;
                wrapper.setLastAccessTime(System.currentTimeMillis());
            }
        } catch (Exception e) {
            logger.error("查询Map缓存失败,cacheKey={0}"+cacheKey,e);
        }

        if (wrapper != null && wrapper.isExpire()) {
            cache.remove(key);
            return null;
        }

        return wrapper;
    }

    public CacheWrapper get(CacheKey cacheKey, Callable<CacheWrapper> callable) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return null;
        }

        CacheWrapper wrapper = null;
        if (cache.containsKey(key)) {
            wrapper = (CacheWrapper) cache.get(key);
        } else {
            synchronized (cache) {
                try {
                    if (!cache.containsKey(key)) {
                        wrapper = callable.call();
                    } else {
                        wrapper = (CacheWrapper) cache.get(key);
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }

        if (wrapper != null) {
            wrapper.setLastAccessTime(System.currentTimeMillis());
        }
        return wrapper;
    }

    @Override
    public Long delete(CacheKey cacheKey) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return 0L;
        }
        Object obj = cache.remove(key);

        return obj == null ? 0L : 1L;
    }

    public ConcurrentHashMap<String, Object> getCache() {
        return cache;
    }

    @Override
    public void clear() {
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void shutdown() {
        interruptDaemon();
        daemon.persistCache();
        clear();
    }

    private synchronized void startDaemon() {
        if (daemonThread == null) {
            daemonThread = new Thread(daemon, "map-cache-daemon");
            daemonThread.setDaemon(true);
            daemonThread.start();
        }
    }

    private synchronized void interruptDaemon() {
        daemon.setRun(false);
        if (daemonThread != null) {
            daemonThread.interrupt();
        }
    }

    @Override
    public CacheConfig getConfig() {
        return config;
    }

    @Override
    public Long setMutex(CacheKey cacheKey) {
        throw new UnsupportedOperationException("Map缓存不支持此方法");
    }

    public MapCacheDaemon getDaemon() {
        return daemon;
    }
}
