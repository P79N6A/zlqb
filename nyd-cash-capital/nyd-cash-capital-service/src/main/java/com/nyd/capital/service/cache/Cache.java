package com.nyd.capital.service.cache;


import com.nyd.capital.service.cache.config.CacheConfig;
import com.nyd.capital.service.cache.model.CacheKey;
import com.nyd.capital.service.cache.model.CacheWrapper;

/**
 * Cong Yuxiang
 * 2017/11/21
 */
public interface Cache {


    void set(CacheKey cacheKey, CacheWrapper wrapper);


    CacheWrapper get(CacheKey cacheKey);


    Long delete(CacheKey cacheKey);


    void clear();

    /**
     * 关闭缓存 
     */
    void shutdown();

    /**
     * 获取缓存配置
     */
    CacheConfig getConfig();


    Long setMutex(CacheKey cacheKey);

}
