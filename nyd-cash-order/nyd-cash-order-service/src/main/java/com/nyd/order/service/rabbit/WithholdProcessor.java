package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.order.dao.WithholdOrderDao;
import com.nyd.order.dao.mapper.WithholdOrderMapper;
import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.dto.CreatePayOrderDto;
import com.nyd.order.model.dto.RequestData;
import com.nyd.order.model.dto.SubmitWithholdDto;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.order.service.util.HttpUtil;
import com.nyd.order.service.util.OrderProperties;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
public class WithholdProcessor extends BaseProcesser implements RabbitmqMessageProcesser<OrderMessage> {

    Logger logger = LoggerFactory.getLogger(WithholdProcessor.class);

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private WithholdOrderMapper withholdOrderDao;
    @Autowired
    private OrderProperties orderProperties;
    @Override
    public void processMessage(OrderMessage message) {
        logger.info("代扣mq接受到消息，借款确认后进行代扣逻辑处理,message is" +message.toString());
        try {
            //调用代扣订单生成接口
            String url = "http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/createOrder";
            CreatePayOrderDto createPayOrderDto = new CreatePayOrderDto();
            createPayOrderDto.setBankcardNo(message.getBankAccount());
            createPayOrderDto.setBusinessOrderNo(message.getOrderNo());
            createPayOrderDto.setBusinessOrderType("评估报告");
            createPayOrderDto.setIdNumber(message.getIdNumber());
            createPayOrderDto.setMobile(message.getAccountNumber());
            createPayOrderDto.setPayAmount(Double.valueOf(message.getPayCash().toString()));
            createPayOrderDto.setPayChannelCode("fuiou");
            if(StringUtils.isBlank(message.getAppName())){
                createPayOrderDto.setMerchantCode("xxd");
            }else{
                createPayOrderDto.setMerchantCode(message.getAppName());
            }
            createPayOrderDto.setPayType(1);
            createPayOrderDto.setCallbackUrl(orderProperties.getWithholdCallbackUrl());
            RequestData requestData = new RequestData();
            requestData.setData(createPayOrderDto);
            requestData.setRequestAppId("xxd");
            requestData.setRequestId(UUID.randomUUID().toString());
            requestData.setRequestTime(JSON.toJSONString(new Date()));
            String json = JSON.toJSONString(requestData);
            String payOrderNo = null;
            WithholdOrder withholdOrder = new WithholdOrder();
            //此处设置随便
            withholdOrder.setMemberId(message.getAccountNumber());
            withholdOrder.setPayAmount(message.getPayCash());
            withholdOrder.setUserId(message.getUserId());
            withholdOrder.setAppName(message.getAppName());
            try {
                String sendPost = doCreateWithhold(url, json);
                JSONObject jsonObject = JSONObject.parseObject(sendPost);
                JSONObject data = jsonObject.getJSONObject("data");
                payOrderNo = data.getString("payOrderNo");
                if (StringUtils.isBlank(payOrderNo)) {
                    throw new RuntimeException("withholdorder payOrderNo is null error");
                }
            } catch (Exception e) {
                logger.error("调用生成代扣订单接口发生异常",e);
                throw new RuntimeException("create withholdorder error");
            }
            withholdOrder.setPayOrderNo(payOrderNo);
            //调用代扣接口
            SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
            String url1 = "http://" + orderProperties.getCommonPayIp() + ":" + orderProperties.getCommonPayPort() + "/common/pay/submitWithhold";
            logger.info("代扣请求地址"+url1);
            submitWithholdDto.setPayOrderNo(payOrderNo);
            submitWithholdDto.setWithholdAmount(Double.valueOf(message.getPayCash().toString()));
            requestData.setData(submitWithholdDto);
            requestData.setRequestAppId("xxd");
            requestData.setRequestId(UUID.randomUUID().toString());
            requestData.setRequestTime(JSON.toJSONString(new Date()));
            String json1 = JSON.toJSONString(requestData);
            String withholdOrderNo = null;
            try {
                String sendPost = doSubmit(url1, json1);
                logger.info("代扣相应结果"+sendPost);
                JSONObject jsonObject = JSONObject.parseObject(sendPost);
                JSONObject data = jsonObject.getJSONObject("data");
                withholdOrderNo = data.getString("withholdOrderNo");
                withholdOrder.setWithholdOrderNo(withholdOrderNo);
                withholdOrder.setOrderStatus(1);
                withholdOrderDao.save(withholdOrder);
            } catch (Exception e) {
                logger.error("调用发起代扣订单接口发生异常",e);
                withholdOrder.setOrderStatus(0);
                withholdOrderDao.save(withholdOrder);
            }
//            //往member发mq
//            MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
//            memberModel.setMobile(message.getAccountNumber());
//            memberModel.setUserId(message.getUserId());
//            memberModel.setMemberType(message.getMemberType());
//            memberModel.setDebitFlag("2");
//            memberModel.setPayCash(message.getPayCash());
//            memberModel.setMemberId(message.getMemberId());
//            memberModel.setDebitChannel("fuiou");
//            memberModel.setAppName(message.getAppName());
//            try {
//                rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
//                logger.info("发送member_MQ成功");
//            } catch (Exception e) {
//                logger.info("给member发送mq消息的对象异常", e);
//            }
        }catch (Exception e){
            logger.error("借款确认后进行代扣逻辑处理发生异常",e);
        }
    }

    private String doCreateWithhold(String url, String jsonStr) {
        int retryCount=4;
        String result = null;
        do {
            try {
                result = HttpUtil.sendPost(url,jsonStr);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject orderResultData = jsonObject.getJSONObject("data");
                    String payOrderNo = orderResultData.getString("payOrderNo");
                    if(StringUtils.isNotBlank(payOrderNo)) {
                        return result;
                    } else {
                        TimeUnit.MILLISECONDS.sleep(500);
                        retryCount--;
                    }
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                    retryCount--;
                }
            } catch (Exception e) {
                logger.error("doCreateWithhold has exception",e);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e1) {
                    logger.error("doCreateWithhold sleep has error",e1);
                }
                retryCount--;
            }
        } while (retryCount>0);
        return result;
    }

    private String doSubmit(String url, String jsonStr) {
        int retryCount=4;
        String result = null;
        do {
            try {
                result = HttpUtil.sendPost(url,jsonStr);
                if (StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    if(jsonObject!=null) {
                        return result;
                    } else {
                        TimeUnit.MILLISECONDS.sleep(500);
                        retryCount--;
                    }
                } else {
                    TimeUnit.MILLISECONDS.sleep(500);
                    retryCount--;
                }
            } catch (Exception e) {
                logger.error("doSubmit has exception",e);
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e1) {
                    logger.error("doSubmit sleep has error",e1);
                }
                retryCount--;
            }
        } while (retryCount>0);
        return result;
    }

}
