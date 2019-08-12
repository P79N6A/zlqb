package com.nyd.capital.service.cache.map;


import com.nyd.capital.service.cache.config.CacheConfig;
import com.nyd.capital.service.cache.serializer.HessianSerializer;
import com.nyd.capital.service.cache.serializer.Serializer;
import com.nyd.capital.service.utils.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**

 * Cong Yuxiang
 * 2017/11/21
 */
public class MapCacheDaemon implements Runnable{

    private static final Logger logger   = LoggerFactory.getLogger(MapCacheDaemon.class);

    private final MapCache           mapCache;

    private final CacheConfig config;

    private final Serializer<Object> serializer;

    private boolean                  isRun    = true;

    private final String             fileName = "map.cache";

    public MapCacheDaemon(MapCache mapCache, CacheConfig config) {
        logger.info( "MapCacheDaemon init...");
        this.mapCache = mapCache;
        this.config = config;
        this.serializer = new HessianSerializer<Object>();
        logger.info("MapCacheDaemon init success!");
    }

    @Override
    public void run() {
        while (isRun) {

            try {
                Thread.sleep(config.getTimeBetweenPersist() * 1000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
            //持久化缓存
            persistCache();
        }
    }





    public void persistCache() {
        if (!config.isPersist()) {
            return;
        }
//        if(mapCache.getCache().size()==0){
//            return;
//        }
//        logger.info("开始持久化缓存");
        File file = createCacheFile();
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new FileOutputStream(file);
            bos = new BufferedOutputStream(out);
            bos.write(serializer.serialize(mapCache.getCache()));
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                bos.close();
                out.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
//        logger.info("缓存持久化完成");
    }

    @SuppressWarnings("unchecked")
    public void readCacheFromDisk() {
        logger.info("开始从磁盘读取缓存");
        String filePath = getSavePath() + fileName;
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            InputStream in = null;
            BufferedInputStream bis = null;
            ByteArrayOutputStream baos = null;
            try {
                in = new FileInputStream(file);
                bis = new BufferedInputStream(in);
                baos = new ByteArrayOutputStream();

                byte[] bytes = new byte[1024];
                while (bis.read(bytes) != -1) {
                    baos.write(bytes);
                }

                Object obj = serializer.deserialize(baos.toByteArray());
                if (obj instanceof ConcurrentHashMap) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) obj;
                    mapCache.getCache().putAll(map);
                    logger.info("缓存读取了{0}个对象", map.size());
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                try {
                    bis.close();
                    in.close();
                    baos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        logger.info("缓存读取完毕");
    }

    public File createCacheFile() {
        String folderPath = getSavePath();
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filePath = folderPath + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return file;
    }

    /**
     * 获取缓存存储路径
     */
    private String getSavePath() {
        String configPath = config.getPersistecePath();
        String savePath = "";
        if (OSUtil.isLinux()) {
            savePath = configPath + File.separatorChar;
        } else if (OSUtil.isWindows()) {
            savePath = "D:" + configPath + File.separatorChar;
        }
        return savePath;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }

}
