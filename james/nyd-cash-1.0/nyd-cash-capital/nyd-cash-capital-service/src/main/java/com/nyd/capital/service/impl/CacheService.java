package com.nyd.capital.service.impl;

import com.nyd.capital.service.ICacheService;
import com.nyd.capital.service.cache.map.MapCache;
import com.nyd.capital.service.cache.model.CacheKey;
import com.nyd.capital.service.cache.model.CacheWrapper;
import com.tasfe.framework.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cong Yuxiang
 * 2017/11/21
 **/
@Service
public class CacheService implements ICacheService{
    private static final String LIST_KEY = "CAPITAL_LIST";
    Logger logger = LoggerFactory.getLogger(CacheService.class);
    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private MapCache cache = new MapCache();

    @Override
    public void setKey(String key) {
        try {

            Long flag = redisTemplate.opsForList().leftPush(LIST_KEY, key);
            logger.info("******************" + flag);
            ConcurrentHashMap<String, Object> map = cache.getCache();
            if(map.size()==0){
                return;
            }
            for(Map.Entry<String,Object> entry:map.entrySet()){
                redisTemplate.opsForList().leftPush(LIST_KEY,entry.getKey());
            }
            map.clear();
            cache.getDaemon().persistCache();

        }catch(Exception e){
            CacheWrapper wrapper = new CacheWrapper("1");
            CacheKey cacheKey = new CacheKey(key);

            cache.set(cacheKey,wrapper);
        }

    }

    @Override
    public void deleteKey(String key) {
        try {
            CacheKey cacheKey = new CacheKey(key);
            cache.delete(cacheKey);
            Long count = redisTemplate.opsForList().remove(LIST_KEY, 100, key);
            ConcurrentHashMap<String, Object> map = cache.getCache();
            if(map.size()==0){
                return;
            }
            for(Map.Entry<String,Object> entry:map.entrySet()){
                redisTemplate.opsForList().leftPush(LIST_KEY,entry.getKey());
            }
            map.clear();
            cache.getDaemon().persistCache();

        }catch (Exception e){
            return;
        }


    }

    @PreDestroy
    public void close() {
        logger.info("capital destory........");
        cache.shutdown();
    }

}
