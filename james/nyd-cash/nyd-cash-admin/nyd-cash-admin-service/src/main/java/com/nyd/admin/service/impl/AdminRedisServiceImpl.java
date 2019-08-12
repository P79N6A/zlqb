package com.nyd.admin.service.impl;

import com.nyd.admin.service.AdminRedisService;
import com.tasfe.framework.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * Created by Dengw on 2018/1/4
 */
@Service
public class AdminRedisServiceImpl implements AdminRedisService {
    private static Logger LOGGER = LoggerFactory.getLogger(AdminRedisServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean judgeTimeout(String accountNo,String token) {
        LOGGER.info("begin to judge login timeout, token is " + token);
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            //byte[] asBytes = Base64.getDecoder().decode(token);
            //String decodeToken = new String(asBytes, "utf-8");
            String tokenInRedis = redisService.getString("admin"+accountNo);
            //LOGGER.info("token in redis is " + tokenInRedis);
            if (StringUtils.isBlank(tokenInRedis)) {
                return false;
            } else {
                if (token.equals(tokenInRedis)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("redis get error! key is "+token);
        }
        return false;
    }
}
