package com.nyd.zeus.service.impls;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.zeus.service.AuthService;
import com.tasfe.framework.redis.RedisService;

/**
 * Created by Dengw on 2017/12/8
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private RedisService redisService;

    /**
     * 判断是否登录超时
     * @param accountNumber,deviceId
     * @return boolean false超时，true未超时
     */
    @Override
    public boolean judgeTimeout(String accountNumber,String deviceId){
        if (StringUtils.isBlank(accountNumber)||StringUtils.isBlank(deviceId)) {
            return false;
        } else {
            try {
                String deviceIdInRedis = redisService.getString(accountNumber);
                if (StringUtils.isBlank(deviceIdInRedis)) {
                    return false;
                } else {
                    if (deviceId.equals(deviceIdInRedis)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("redis get has except! key is "+accountNumber);
            }
        }
        return false;
    }
}
