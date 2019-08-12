package com.creativearts.nyd.pay.service.impl;

import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.RedisProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2018/4/19
 **/
@Service
public class RedisProcessServiceImpl implements RedisProcessService{

    final Logger logger = LoggerFactory.getLogger(RedisProcessServiceImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PayStatus checkIfPay(String billNo) {
        try {
            String status = (String) redisTemplate.opsForValue().get(Constants.PAY_PREFIX + billNo);
            if (status == null) {
                //代表还款中
                redisTemplate.opsForValue().set(Constants.PAY_PREFIX + billNo, PayStatus.PAYING.getCode(), 30, TimeUnit.MINUTES);
                return PayStatus.UN_PAY;
            } else {
                if (status.equals(PayStatus.PAY_SUCESS.getCode())) {
                    return PayStatus.PAY_SUCESS;
                } else {
                    return PayStatus.PAYING;
                }
            }
        }catch (Exception e){
            return PayStatus.UN_PAY;
        }
    }

    @Override
    public void putPayStatusMember(String userId,String value) {
        try {
//            redisTemplate.opsForValue().set(Constants.PAY_STATUS + userId, value, 144, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(Constants.PAY_STATUS + userId, value, 1, TimeUnit.MINUTES);
        }catch (Exception e){
            logger.error("保存支付状态异常",e);
            e.printStackTrace();
        }

    }

    @Override
    public String getPayStatusMember(String userId) {
        return (String) redisTemplate.opsForValue().get(Constants.PAY_STATUS+userId);
    }

    @Override
    public void delPayStatusMember(String userId) {

        redisTemplate.delete(Constants.PAY_STATUS+userId);

    }
}
