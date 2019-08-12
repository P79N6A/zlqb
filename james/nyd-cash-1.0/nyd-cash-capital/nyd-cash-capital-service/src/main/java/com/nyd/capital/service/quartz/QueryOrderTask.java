package com.nyd.capital.service.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2017/11/17
 **/
@Component("queryOrderTask")
public class QueryOrderTask {
//    @Autowired
//    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public void run() {
//        System.out.println("**********");
//       redisTemplate.opsForList().size();
    }
}
