package com.nyd.capital.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.nyd.capital.model.pocket.PocketHtmlMessage;
import com.nyd.capital.service.mq.PocketHtmlProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author zhangdk
 */
@Service
public class KdlcFundService implements FundService {

    Logger logger = LoggerFactory.getLogger(KdlcFundService.class);

    @Autowired
    private OrderContract orderContract;
    @Autowired
    private PocketHtmlProducer producer;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseData sendOrder(List orderList) {
        logger.info("sendOrder params:" + JSON.toJSON(orderList));
        ResponseData remitStatus = ResponseData.success();
        try {
            if (orderList.size() == 0) {
                return ResponseData.error();
            }
            // 资产提交
            String orderNo = (String) orderList.get(1);
            String userId = (String) orderList.get(0);
            // 借款请求成功
            OrderInfo orderInfo = null;
            try {
                orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
                if (orderInfo == null) {
                    logger.info("根据订单号号查询订单不存在");
                    return ResponseData.error();
                }
            } catch (Exception e) {
                logger.error("根据订单号查询订单发生异常");
                return ResponseData.error();
            }
            logger.info("orderInfo:" + JSON.toJSONString(orderInfo));
            if (redisTemplate.hasKey(Constants.KDLC_CALLBACK_PREFIX + orderNo)) {
                logger.error("重复调用生成订单接口");
                return ResponseData.error("请勿重复提交");
            } else {
                redisTemplate.opsForValue().set(Constants.KDLC_CALLBACK_PREFIX + orderNo, "1",24*60,TimeUnit.MINUTES);
            }
            //进行订单确认队列
            PocketHtmlMessage message = new PocketHtmlMessage();
            message.setUserId(userId);
            message.setOrderNo(orderNo);
            logger.info("send mq to pocket order confirm html and message is:" + message.toString());
            producer.sendMsg(message);
        } catch (Exception e) {
            logger.error("推送资产到口袋理财出错啦!", e);
        }
        return remitStatus;
    }

    @Override
    public boolean saveLoanResult(String result) {
        return false;
    }

    @Override
    public String queryOrderInfo(WsmQuery query) {
        return null;
    }

    @Override
    public List generateOrdersTest() {
        return null;
    }

    @Override
    public List generateOrders(String userId, String orderNo, Integer channel) throws ValidateException {
        List list = new ArrayList();
        list.add(userId);
        list.add(orderNo);
        return list;
    }
}
