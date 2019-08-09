package com.nyd.settlement.service.impl;

import com.nyd.settlement.service.SettlementRedisService;
import com.tasfe.framework.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hwei on 2018/1/20.
 */
@Service
public class SettlementRedisServiceImpl implements SettlementRedisService {
    private static Logger LOGGER = LoggerFactory.getLogger(SettlementRedisServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Override
    public boolean judgeTimeout(String accountNo,String token) {
        LOGGER.info("begin to judge login timeout, token is " + token);
        if (StringUtils.isBlank(token)) {
            return false;
        }
        try {
            String tokenInRedis = redisService.getString("admin"+accountNo);
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
