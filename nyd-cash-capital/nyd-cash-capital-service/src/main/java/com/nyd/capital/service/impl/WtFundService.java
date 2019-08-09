package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.model.enums.RemitStatus;
import com.nyd.capital.model.wt.WtCallbackResponse;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.utils.LoggerUtils;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderWentongContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderWentongInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Cong Yuxiang
 * 2018/4/26
 **/
@Service
public class WtFundService implements FundService {

    final Logger logger = LoggerFactory.getLogger(WtFundService.class);

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    private OrderWentongContract orderWentongContract;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private UserIdentityContract userIdentityContract;

    @Override
    public ResponseData sendOrder(List orderList) {
        Iterator<OrderWentongInfo> ite = orderList.iterator();

        while (ite.hasNext()) {
            OrderWentongInfo orderWentongInfo = ite.next();
            ResponseData responseData = orderWentongContract.save(orderWentongInfo);
            if ("0".equals(responseData.getStatus())) {
                return ResponseData.success();
            } else {
                return ResponseData.error();
            }
        }
        return ResponseData.error();
    }

    @Override
    public boolean saveLoanResult(String result){
        WtCallbackResponse response = JSONObject.parseObject(result, WtCallbackResponse.class);
        logger.info("wt json转对象" + JSON.toJSONString(response));

        Integer channel = Integer.valueOf(response.getOrderNo().charAt(response.getOrderNo().length() - 1) + "");
        logger.info("channel" + channel);
        String orderNo = response.getOrderNo().substring(0, response.getOrderNo().length() - 1);


        try {
            if (redisTemplate.hasKey(Constants.WT_CALLBACK_PREFIX + response.getOrderNo())) {
                logger.error("有重复通知" + JSON.toJSONString(response));
                return true;
            } else {
                redisTemplate.opsForValue().set(Constants.WT_CALLBACK_PREFIX + response.getOrderNo(), "1", 28800, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("写redis出错" + e.getMessage());
        }

        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus(response.getStatus());

        remitMessage.setRemitAmount(response.getAmount());

//        if(channel==BorrowConfirmChannel.NYD.getChannel()){
        remitMessage.setOrderNo(orderNo);
        rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
//        }


        OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
        int loopCount = 10;
        while (orderInfo == null && loopCount > 0) {
            orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();
            loopCount--;
        }
        if (orderInfo == null) {
            logger.error(orderNo + "orderinfo为空");
            return false;
        }

        logger.info("orderInfo:" + JSON.toJSONString(orderInfo));

        //如果是null 默认为nyd的订单来源
        if (orderInfo.getChannel() == null) {
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }

        //发送 到 ibank
        if (orderInfo.getChannel() == BorrowConfirmChannel.YMT.getChannel()) {

            remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
            logger.info("放款结果发送ibank." + JSON.toJSONString(remitMessage));
            rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
        }


        // Update by Jiawei Cheng 2018-3-29 20:48 如果是银码头的来源，不进行扣会员费
        //会员费前置 不主动扣会员费
//        if(orderInfo.getMemberFee().compareTo(new BigDecimal("0.0"))==1 && BorrowConfirmChannel.NYD.getChannel().equals(orderInfo.getChannel())){
//            CreateOrderVo vo = new CreateOrderVo();
//            vo.setP3_orderId(orderInfo.getOrderNo()+"_"+(System.currentTimeMillis()+"").substring(2,11));
//            vo.setP4_timestamp(DateFormatUtils.format(new Date(),"yyyyMMddHHmmss"));
//            vo.setP5_payerName(orderDetailInfo.getRealName());
//            vo.setP7_idCardNo(orderDetailInfo.getIdNumber());
//            vo.setP8_cardNo(orderInfo.getBankAccount());
//            vo.setP11_orderAmount(orderInfo.getMemberFee());
//            logger.info("代扣会员费请求参数:"+JSON.toJSONString(vo));
//            ResponseData r = payService.withHold(vo, WithHoldType.MEMBER_FEE);
//            logger.info("会员费 代扣结果"+JSON.toJSONString(r));
//        }


        RemitInfo remitInfo = new RemitInfo();

        try {
            remitInfo.setRemitTime(DateUtils.parseDate(response.getRemitTime(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
            logger.info(orderNo + "稳通放款时间转化异常" + response.getRemitTime());
        }
        remitInfo.setOrderNo(orderNo);
        remitInfo.setRemitStatus(response.getStatus());
        remitInfo.setFundCode("wt");
        remitInfo.setRemitNo(response.getRecordNo());
        remitInfo.setContractLink(response.getWithDrawNo());
        remitInfo.setChannel(orderInfo.getChannel());
        remitInfo.setRemitAmount(response.getAmount());

        try {
            rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerUtils.write(remitInfo);



        return true;
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
        AccountDto accountDto = userAccountContract.getAccount(userId).getData();
        int loopCount = 10;
        while (accountDto == null && loopCount > 0) { // 防止dubbo出现错误
            accountDto = userAccountContract.getAccount(userId).getData();
            loopCount--;
        }
        if (accountDto == null) {
            logger.error("userId:" + userId + "orderNo:" + orderNo + "accountDto为null");
        }

        UserInfo userInfo = userIdentityContract.getUserInfo(userId).getData();
        OrderInfo orderInfo = orderContract.getOrderByOrderNo(orderNo).getData();

        OrderWentongInfo orderWentongInfo = new OrderWentongInfo();
        orderWentongInfo.setMobile(accountDto.getAccountNumber());
        orderWentongInfo.setOrderNo(orderNo + channel);
        orderWentongInfo.setUserId(userId);
        orderWentongInfo.setName(userInfo.getRealName());
        orderWentongInfo.setLoanTime(DateFormatUtils.format(orderInfo.getLoanTime(),"yyyy-MM-dd HH:mm:ss"));


        list.add(orderWentongInfo);
        return list;
    }
}
