/**
 * 深圳金融电子结算中心
 * Copyright (c) 1995-2017 All Rights Reserved.
 */
package com.nyd.capital;


import com.alibaba.fastjson.JSON;
import com.nyd.capital.service.cache.config.CacheConfig;
import com.nyd.capital.service.cache.map.MapCache;
import com.nyd.capital.service.cache.model.CacheKey;
import com.nyd.capital.service.cache.model.CacheWrapper;
import org.junit.After;
import org.junit.Test;

/**

 */
public class MapCacheTest {

    private CacheConfig cacheConfig = new CacheConfig();

    private MapCache cache;



//    @Before
//    public void init() {
//        cache = new MapCache(cacheConfig);
//    }

//    @Test
//    public void testSetAndGet() throws Exception {
//        Assert.assertNotNull(cache);
//        CacheKey cacheKey = new CacheKey("key123");
//
//        CacheWrapper wrapper = new CacheWrapper("value123");
//
//        //测试普通set
//        cache.set(cacheKey, wrapper);
//        CacheWrapper ret = cache.get(cacheKey);
//
//        System.out.println(JSON.toJSONString(ret));
//
//
//    }
    @Test
    public void testStart(){
        cache = new MapCache(cacheConfig);
        CacheKey cacheKey = new CacheKey("key123");
        CacheWrapper ret = cache.get(cacheKey);
        CacheKey cacheKey1 = new CacheKey("key123678");
        CacheWrapper wrapper = new CacheWrapper("value123dgasd");
        cache.set(cacheKey1,wrapper);
        System.out.println(JSON.toJSONString(ret));
    }


    @After
    public void shutdown() {
        cache.shutdown();
    }

}
