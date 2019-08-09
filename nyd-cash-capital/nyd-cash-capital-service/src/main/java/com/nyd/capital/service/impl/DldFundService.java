package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.entity.UserDld;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.WsmQuery;
import com.nyd.capital.service.FundService;
import com.nyd.capital.service.UserDldService;
import com.nyd.capital.service.dld.service.DldService;
import com.nyd.capital.service.dld.utils.JsonUtils;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.validate.ValidateException;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.dto.AccountDto;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DldFundService implements FundService {

    Logger logger = LoggerFactory.getLogger(DldFundService.class);

    @Autowired
    private DldService dldService;
    @Autowired
    private UserDldService userDldService;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private UserAccountContract userAccountContract;
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
            String customerId = (String) orderList.get(2);

            //借款请求成功
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

            if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + orderNo)) {
                logger.error("重复调用签约接口");
                throw new Exception("重复调用签约接口");
            } else {
                redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + orderNo, "1",10,TimeUnit.HOURS);
            }

            ResponseData responseData = dldService.loanSubmitByOrderNo(userId, orderNo, customerId);
            if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                return ResponseData.error();
            }
            String dataData = (String) responseData.getData();
            JSONObject data = JSON.parseObject(dataData);
            JSONObject jsonObject = data.getJSONObject("data");
            if (jsonObject == null) {
                orderInfo.setOrderStatus(40);
              //生成异常订单记录
                try {
                	orderExceptionContract.saveByOrderInfo(orderInfo);
                }catch(Exception e) {
                	logger.error("生成异常订单信息异常：" + e.getMessage());
                }
                orderContract.updateOrderInfo(orderInfo);
                String msg = data.getString("msg");
                orderInfo.setPayFailReason(msg);
                orderContract.updateOrderInfo(orderInfo);
                //DingdingUtil.getErrMsg("多来点发起借款申请失败," + "msg:" + msg + ",订单号为:" + orderNo);
                remitStatus.setStatus("2");
                remitStatus.setError(msg);
                return remitStatus;
            }
            String respCode = jsonObject.getString("respCode");

            if ("TS1001".equals(respCode)) {
            	//借款请求成功
                OrderInfo order = null;
                try {
                    order = orderContract.getOrderByOrderNo(orderNo).getData();
                    if (order == null) {
                        logger.info("根据订单号号查询订单不存在");
                    }
                    if(order.getOrderStatus().equals(50)) {
                    	return remitStatus;
                    }
                } catch (Exception e) {
                    logger.error("根据订单号查询订单发生异常");
                }
            	String orderId = jsonObject.getString("orderId");
            	 /*if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + orderId)) {
                     logger.error("dld借款结果已处理");
                     throw new Exception("dld借款结果已处理");
                 } else {
                     redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + orderId, "1",10,TimeUnit.HOURS);
                 }*/
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
                    logger.info("放款成功发送ibank." + JSON.toJSONString(remitMessage));
                    rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
                }

                RemitInfo remitInfo = new RemitInfo();
                remitInfo.setRemitTime(new Date());
                remitInfo.setOrderNo(orderInfo.getOrderNo());
                remitInfo.setRemitStatus("0");
                remitInfo.setFundCode("dld");
                remitInfo.setChannel(orderInfo.getChannel());
                remitInfo.setRemitNo(orderId);
                remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                logger.info("多来点放款流水:" + JSON.toJSON(remitInfo));
                try {
                    rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                    logger.info("多来点放款流水发送mq成功");
                } catch (Exception e) {
                    logger.error("发送mq消息发生异常");
                    DingdingUtil.getErrMsg("多来点放款成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(orderList));
                }

            } else if ("TS1111".equals(respCode)) {
                remitStatus = ResponseData.success();
                redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + orderNo);
            } else {
                //借款失败
                DingdingUtil.getErrMsg("多来点发起借款申请失败,订单号为:" + orderNo + "；错误码：" + respCode + "；msg：" + jsonObject.getString("respMsg"));
                remitStatus = ResponseData.error();
                orderInfo.setPayFailReason(orderInfo.getFundCode() + "[" + respCode + ":" + jsonObject.getString("respMsg") + "]");
                orderInfo.setOrderStatus(40);
              //生成异常订单记录
                orderExceptionContract.saveByOrderInfo(orderInfo);
                orderContract.updateOrderInfo(orderInfo);
                redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + orderNo);
                remitStatus.setStatus("2");
                return remitStatus;
            }

        } catch (Exception e) {
            logger.error("推送资产到多来点出错啦!", e);
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
        try {
            String customerId = null;
            UserDld userDldByUserId = userDldService.getUserDldByUserId(userId);
            ResponseData<AccountDto> account = userAccountContract.getAccount(userId);
            if (OpenPageConstant.STATUS_ONE.equals(account.getStatus())) {
                return list;
            }
            AccountDto accountDto = account.getData();
            if (userDldByUserId == null) {
                ResponseData responseData = dldService.registerUserByUserId(userId, accountDto.getIBankUserId());
                if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                    logger.error("多来点注册用户时发生异常" + JSON.toJSONString(userId));
                    return list;
                }
                String data = (String) responseData.getData();
                customerId = JsonUtils.getValue(data, 3, new String[]{"data", "data", "customerId"}).toString();
            } else {
                customerId = userDldByUserId.getDldCustomerId();
            }
            list.add(userId);
            list.add(orderNo);
            list.add(customerId);
        } catch (Exception e) {
            logger.error("generateOrders has error", e);
        }
        return list;
    }
}
