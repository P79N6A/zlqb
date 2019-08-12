/**
 * 深圳金融电子结算中心
 * Copyright (c) 1995-2017 All Rights Reserved.
 */
package com.nyd.capital;


import com.alibaba.fastjson.JSON;
import com.nyd.capital.service.cache.config.CacheConfig;
import com.nyd.capital.service.cache.map.MapCache;
import com.nyd.capital.service.cache.map.MapCacheDaemon;
import com.nyd.capital.service.cache.model.CacheKey;
import com.nyd.capital.service.cache.model.CacheWrapper;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class MapCacheDaemonTest {

    private MapCacheDaemon daemon;

    private MapCache mapCache;



    @Before
    public void init() {
        CacheConfig config = new CacheConfig();
        mapCache = new MapCache(config);
        daemon = new MapCacheDaemon(mapCache, config);
        //组装数据

    }

    @Test
    public void testPersistCache() {
        CacheKey cacheKey = new CacheKey("keydae123");
        CacheWrapper wrapper = new CacheWrapper("valuedae123");

        //测试普通set
        mapCache.set(cacheKey, wrapper);
        daemon.persistCache();
    }

    @Test
    public void testReadCache() {
        mapCache.clear();
//        Assert.assertTrue(mapCache.getCache().isEmpty());
        daemon.readCacheFromDisk();
        CacheKey cacheKey = new CacheKey("keydae123");

        CacheWrapper wrapper = mapCache.get(cacheKey);

        System.out.println(JSON.toJSONString(wrapper));
    }

    @Test
    public void testClearCache() throws InterruptedException {
//        int expireTime = 3;
//        CacheKey cacheKey = new CacheKey(emp.getId() + "");
//        CacheWrapper wrapper = new CacheWrapper(emp, expireTime);
//
//        mapCache.set(cacheKey, wrapper);
//        CacheWrapper ret = mapCache.get(cacheKey);
//        Assert.assertNotNull(ret);
//
//        Thread.sleep(expireTime * 1000 + 500);
//
//        daemon.clearCache();
//
//        ret = mapCache.get(cacheKey);
//        Assert.assertNull(ret);
    }

}
