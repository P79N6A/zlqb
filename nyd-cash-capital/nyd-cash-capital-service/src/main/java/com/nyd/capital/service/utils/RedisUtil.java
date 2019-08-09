package com.nyd.capital.service.utils;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.LOCKED;

@Component
public class RedisUtil {

    Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean lock(String key, long timeout) {
        String lock = prefixLock(key);
        boolean locked;
        if (BooleanUtils.isTrue(redisTemplate.opsForValue().setIfAbsent(lock, LOCKED))) {
            redisTemplate.expire(lock, timeout, TimeUnit.MINUTES);
            locked = true;
            logger.debug("add RedisLock[" + lock + "].");
        } else {
            locked = false;
        }
        return locked;
    }


    public void unlock(String key) {
        String lock=prefixLock(key);
        logger.debug("release RedisLock[" + lock + "].");
        redisTemplate.delete(lock);
    }


    private String prefixLock(String key) {
        return "LOCK-" + key;
    }
}
