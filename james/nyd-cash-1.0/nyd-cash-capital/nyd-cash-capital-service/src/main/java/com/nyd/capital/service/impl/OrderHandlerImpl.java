package com.nyd.capital.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.model.enums.KdlcQueryStatusEnum;
import com.nyd.capital.service.OrderHandler;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
@Service("orderHandler")
public class OrderHandlerImpl implements OrderHandler {
	
    @Autowired
    private PocketService pocketService;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;

    Logger logger = LoggerFactory.getLogger(OrderHandlerImpl.class);

	@Override
	public ResponseData orderSuccessHandler(OrderInfo orderInfo) {
		//放款成功,通知zues
        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus("0");
        remitMessage.setRemitAmount(orderInfo.getLoanAmount());
        remitMessage.setOrderNo(orderInfo.getOrderNo());
        rabbitmqProducerProxy.convertAndSend("remit.nyd", remitMessage);
        //如果是null 默认为nyd的订单来源
        if (orderInfo.getChannel() == null) {
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }
        //发送 到 ibank
        if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
            remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
            logger.info("口袋理财放款成功发送ibank." + JSON.toJSONString(remitMessage));
            try {
            	rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
            	logger.info("口袋理财放款成功发送ibank成功");
            }catch (Exception e) {
            	logger.error("口袋理财放款成功发送ibank mq消息发生异常");
                DingdingUtil.getErrMsg("口袋理财放款成功,发送ibank mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(remitMessage));            	
            }
        }
        RemitInfo remitInfo = new RemitInfo();
        remitInfo.setRemitTime(new Date());
        remitInfo.setOrderNo(orderInfo.getOrderNo());
        remitInfo.setRemitStatus("0");
        remitInfo.setFundCode("kdlc");
        remitInfo.setChannel(orderInfo.getChannel());
        remitInfo.setRemitAmount(orderInfo.getLoanAmount());
        logger.info("口袋理财放款流水:" + JSON.toJSON(remitInfo));
        try {
            rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
            logger.info("口袋理财放款流水发送mq成功");
        } catch (Exception e) {
            logger.error("口袋理财发送mq消息发生异常");
            DingdingUtil.getErrMsg("口袋理财放款成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(remitInfo));
        }
        return ResponseData.success();
	}

	@Override
	public ResponseData orderFailHankdler(OrderInfo orderInfo, String channalKey) {
		//借款失败
        DingdingUtil.getErrMsg("口袋理财发起借款申请失败,订单号为:" + orderInfo.getOrderNo());
        orderInfo.setOrderStatus(40);
      //生成异常订单记录
        try {
        	orderExceptionContract.saveByOrderInfo(orderInfo);
        }catch(Exception e) {
        	logger.error("生成异常订单信息异常：" + e.getMessage());
        }
        orderContract.updateOrderInfo(orderInfo);
        redisTemplate.delete(Constants.KDLC_CALLBACK_PREFIX + orderInfo.getOrderNo());
        return ResponseData.error();
	}
	@Override
	public ResponseData orderFailHankdler(OrderInfo orderInfo, String channlKey, ResponseData data) {
		Map<String, Object> dataData = (Map<String, Object>) data.getData();
		orderInfo.setOrderStatus(40);
		//生成异常订单记录
		try {
        	orderExceptionContract.saveByOrderInfo(orderInfo);
        }catch(Exception e) {
        	logger.error("生成异常订单信息异常：" + e.getMessage());
        }
		orderContract.updateOrderInfo(orderInfo);
		String msg = (String) dataData.get("message");
		DingdingUtil.getErrMsg("口袋理财发起借款申请失败,"+ "订单号为:" + orderInfo.getOrderNo() + "；错误码：" + dataData.get("code") + "；msg:" + msg );
		return ResponseData.error(msg);
	}

}
