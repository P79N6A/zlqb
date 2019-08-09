package com.nyd.capital.ws.controller;

import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.OrderContract;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Cong Yuxiang
 * 2017/12/25
 **/
@RestController
@RequestMapping("/nyd")
public class TestController {
    Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/test")
    public ResponseData index(){
//        OrderInfo orderInfo = orderContract.getOrderByOrderNo("101512997909890001").getData();
//        if(orderInfo == null){
//            System.out.println("**********************hahah");
//            throw new RuntimeException("hahahaha********************");
////            return ResponseData.error("orderinfo为空");
//        }else {
//            return ResponseData.success(orderInfo);
//        }
        String redisKey = Constants.KZJR_PREFIX + "20171222105087819541856256"+"_32";
        logger.info(redisTemplate.hasKey(redisKey).toString());
        logger.info(redisTemplate.opsForValue().get(redisKey).toString());
        double d = 7000;
        redisTemplate.opsForValue().set(redisKey,d);
        return ResponseData.success();

    }
}
