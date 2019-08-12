package com.nyd.capital.service.qcgz.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.kzjr.QueryAssetRequest;
import com.nyd.capital.model.kzjr.response.KzjrInvestorResponse;
import com.nyd.capital.model.qcgz.LoanApplyRequest;
import com.nyd.capital.model.qcgz.LoanSuccessNotifyRequest;
import com.nyd.capital.model.qcgz.QcgzInvestorResponse;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.qcgz.business.QcgzBusiness;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.utils.LoggerUtils;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Service
public class QcgzBusinessImpl implements QcgzBusiness {

    Logger logger = LoggerFactory.getLogger(QcgzBusinessImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;

    @Override
    public ResponseData submitLoanApply(LoanApplyRequest request) {
        logger.info("进行七彩格子放款申请,request:" + JSON.toJSONString(request));
        if (StringUtils.isBlank(request.getOrderNo())){
            logger.info("订单号不能为空");
            return ResponseData.error("订单号不能为空");
        }
        //通过订单号查询资产编号和银行卡信息

        return null;
    }

    @Override
    public String callBack(LoanSuccessNotifyRequest request) {
        // 避免重复的通知
        try {
            if (redisTemplate.hasKey(Constants.QCGZ_CALLBACK_PREFIX + request.getAssetId())) {
                logger.error("有重复通知" + JSON.toJSONString(request));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.QCGZ_CALLBACK_PREFIX + request.getAssetId(), "1", 2880, TimeUnit.MINUTES);
            }
        }catch (Exception e){
            logger.error("写redis出错"+e.getMessage());
        }


        //防止跑批中已经处理
        try {
            if (redisTemplate.hasKey(Constants.CAPITAL_QUERY_KEY + request.getAssetId())) {
                logger.error("跑批中已经处理" + JSON.toJSONString(request));
                return "success";
            } else {
                redisTemplate.opsForValue().set(Constants.CAPITAL_QUERY_KEY + request.getAssetId(), "1", 2880, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            logger.error("写redis出错" ,e);
        }
        return pushZues(request);
    }

    @Override
    public String pushZues(LoanSuccessNotifyRequest request){
        List<QcgzInvestorResponse> investorList = request.getInvestorList();
        QcgzInvestorResponse qcgzInvestorResponse = investorList.get(0);

        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus("0");
        OrderInfo orderInfo = null;
        try {
            orderInfo = capitalOrderRelationContract.selectOrderInfo(request.getAssetId()).getData();
            if (orderInfo == null){
                logger.info("根据资产号查询订单不存在");
                return "fail";
            }
        }catch (Exception e){
            logger.error("根据资产号查询订单发生异常");
            return "fail";
        }
        logger.info("orderInfo:"+JSON.toJSONString(orderInfo));
        remitMessage.setRemitAmount(orderInfo.getLoanAmount());
        if (request.getLoanResult() == 0) {
            //0 表示放款成功
            remitMessage.setOrderNo(orderInfo.getOrderNo());
            rabbitmqProducerProxy.convertAndSend("remit.nyd",remitMessage);
        }

        //如果是null 默认为nyd的订单来源
        if(orderInfo.getChannel() == null){
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }

        //发送 到 ibank
        if(orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())){
            if (request.getLoanResult() == 0) {
                remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
                logger.info("放款成功发送ibank."+JSON.toJSONString(remitMessage));
                rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt",remitMessage);
            }
        }

        for(QcgzInvestorResponse investor:investorList){
            RemitInfo remitInfo = new RemitInfo();

            remitInfo.setRemitTime(new Date());
            remitInfo.setOrderNo(orderInfo.getOrderNo());
            if (request.getLoanResult() == 0) {
                remitInfo.setRemitStatus("0");
            } else {
                remitInfo.setRemitStatus("1");
            }
            remitInfo.setFundCode("qcgz");
            remitInfo.setRemitNo(request.getAssetId());
            remitInfo.setChannel(orderInfo.getChannel());
            remitInfo.setRemitAmount(investor.getAmount());
            remitInfo.setInvestorId(investor.getInvestorId());
            remitInfo.setInvestorName(investor.getInvestorName());
            logger.info("放款流水:"+JSON.toJSON(remitInfo));
            try {
                rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                logger.info("放款流水发送mq成功");
            } catch (Exception e) {
                logger.error("发送mq消息发生异常");
                DingdingUtil.getErrMsg("七彩放款后发送mq消息生产放款记录失败,request is "+JSON.toJSONString(request));
            }
        }
        return "success";
    }
}
