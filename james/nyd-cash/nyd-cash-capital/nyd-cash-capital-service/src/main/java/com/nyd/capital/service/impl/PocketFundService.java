package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PocketFundService implements FundService {

    Logger logger = LoggerFactory.getLogger(PocketFundService.class);

    @Autowired
    private PocketService pocketService;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;

    @Override
    public ResponseData sendOrder(List orderList) {
        logger.info("sendOrder params:" + JSON.toJSON(orderList));
        ResponseData remitStatus = ResponseData.success();
        try {
            if (orderList.size() == 0) {
                return ResponseData.error();
            }
            //资产提交
            String orderNo = (String) orderList.get(1);
            String userId = (String) orderList.get(0);
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(orderNo);
            orderInfo.setUserId(userId);
            if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + orderNo)) {
                logger.error("重复调用签约接口");
                throw new Exception("重复调用签约接口");
            } else {
                redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + orderNo, "1",24*60,TimeUnit.MINUTES);
            }

            ResponseData responseData = pocketService.withdraw(orderInfo);
            if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                orderInfo.setOrderStatus(40);
              //生成异常订单记录
                try {
                	orderExceptionContract.saveByOrderInfo(orderInfo);
                }catch(Exception e) {
                	logger.error("生成异常订单信息异常：" + e.getMessage());
                }
                orderInfo.setPayFailReason(orderInfo.getFundCode() + "[" + responseData.getMsg() + "]");
                orderContract.updateOrderInfo(orderInfo);
                DingdingUtil.getErrMsg("口袋理财放款款申请失败," + "msg:" + responseData.getMsg() + ",订单号为:" + orderNo);
                return ResponseData.error();
            }
            createOrder(orderInfo);

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
    public List generateOrders(String userId, String orderNo, Integer channel) {
        List list = new ArrayList();
        try {
            list.add(userId);
            list.add(orderNo);
        } catch (Exception e) {
            logger.error("generateOrders has error", e);
            return list;
        }
        return list;
    }

    private void createOrder(OrderInfo orderInfo) {
        //开启一个线程去处理创建订单
        new Thread(() -> {
            try {
                logger.info("口袋理财创建订单,orderInfo:" + orderInfo.toString());
                ResponseData order = pocketService.createOrder(orderInfo);
                if (OpenPageConstant.STATUS_ONE.equals(order.getStatus())){
                    DingdingUtil.getErrMsg("口袋理财创建订单失败，orderInfo:"+orderInfo.toString());
                }
            } catch (Throwable e) {
                logger.error("口袋理财创建订单发生异常,orderInfo:" + orderInfo.toString(), e);
                DingdingUtil.getErrMsg("口袋理财创建订单发生异常，orderInfo:"+orderInfo.toString());
            }
        }).start();
    }
}
