package com.nyd.capital.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.enums.PocketAccountEnum;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.*;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.order.entity.BalanceOrder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.api.service.CapitalApi;
import com.nyd.capital.entity.UserDld;
import com.nyd.capital.model.CapitalMessage;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.qcgz.QueryLoanApplyResultRequest;
import com.nyd.capital.service.dld.config.DldConfig;
import com.nyd.capital.service.dld.service.DldService;
import com.nyd.capital.service.dld.utils.DateUtil;
import com.nyd.capital.service.dld.utils.JsonUtils;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.mq.CapitalRiskProducer;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.nyd.capital.service.pocket.util.PocketOrderUtil;
import com.nyd.capital.service.pocket.util.PocketPayUtil;
import com.nyd.capital.service.thread.SendCapitalRunnable;
import com.nyd.capital.service.utils.Constants;
import com.nyd.capital.service.utils.HttpUtil;
import com.nyd.capital.service.utils.RedisUtil;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderChannelContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.api.OrderExceptionContract;
import com.nyd.order.entity.ChannelProportionConfig;
import com.nyd.order.entity.PockerOrderEntity;
import com.nyd.order.model.JudgeInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.WithholdOrderInfo;
import com.nyd.order.model.WithholdTaskConfig;
import com.nyd.order.model.dto.OrderQcgzDto;
import com.nyd.order.model.dto.RequestData;
import com.nyd.order.model.dto.SubmitWithholdDto;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.enums.WithholdConfigCode;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.nyd.zeus.api.RemitContract;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;


/**
 * @author liuqiu
 */
@Service("capitalApi")
public class CapitalServiceImpl implements CapitalService, CapitalApi {

    Logger logger = LoggerFactory.getLogger(CapitalServiceImpl.class);

    @Autowired
    private FundFactory fundFactory;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDldService userDldService;
    @Autowired
    private DldService dldService;
    @Autowired
    private PocketService pocketService;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private com.ibank.order.api.OrderContract orderContractYmt;
    @Autowired
    private RemitContract remitContract;
    @Autowired
    private DldConfig dldConfig;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private PocketConfig pocketConfig;
    @Autowired
    private PocketOrderUtil pocketOrderUtil;
    @Autowired
    private CapitalRiskProducer capitalRiskProducer;
    @Autowired
    private OrderChannelContract orderChannelContract;
    @Autowired(required = false)
    private OrderExceptionContract orderExceptionContract;
    @Autowired
    private UserPocketService userPocketService;
    @Autowired
    private OrderHandler orderHandler;
    @Autowired
    private Pocket2Service pocket2Service;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData newSendCapital(OrderMessage message, boolean ifAuto) {
        logger.info("接入资金风控后资产推送的入参为：" + JSON.toJSONString(message));
        String fundCode = message.getFundCode();
        if (StringUtils.isEmpty(fundCode)) {
            return ResponseData.error("订单没有分配渠道");
        }
        if ((!ifAuto) && FundSourceEnum.KDLC.getCode().equals(fundCode)) {
            return ResponseData.success();
        }
        ResponseData<ChannelProportionConfig> rChannel = orderChannelContract.getChannelByCode(fundCode);
        ChannelProportionConfig channel = rChannel.getData();
        //判断订单渠道是否需要资金风控，不需要直接推送资产，否则放入资金风控队列
        if (channel.getIfRisk().equals(0)) {
            return sendCapitalAfterRisk(message);
        }
        CapitalMessage cMessage = new CapitalMessage();
        cMessage.setBorrowType("0");
        cMessage.setOrderNo(message.getOrderNo());
        cMessage.setUserId(message.getUserId());
        OrderInfo orderInfo = null;
        if (message.getIfTask() != 1 && message.getChannel() == 1) {
            //return sendCapitalAfterRisk(message);
            cMessage.setProductType("ymt");
            orderInfo = orderContract.getOrderByIbankOrderNo(message.getOrderNo());
            ResponseData<OrderInfo> orderInfoY = orderContract.getOrderByOrderNo(orderInfo.getOrderNo());
            orderInfo = orderInfoY.getData();
        } else {
            cMessage.setProductType("nyd");
            ResponseData<OrderInfo> orderInfoN = orderContract.getOrderByOrderNo(message.getOrderNo());
            orderInfo = orderInfoN.getData();
        }
        if (orderInfo == null) {
            return ResponseData.error("没有查询到订单信息");
        }
        try {
            if (orderInfo.getIfRisk() != null && orderInfo.getIfRisk() >= 1) {
                return ResponseData.error("请勿重复提交申请");
            }
            //更新订单是否推送资金风控的状态
            orderInfo.setIfRisk(1);
            orderContract.updateOrderInfo(orderInfo);
            //放入资金风控队列
            logger.info("推送资金风控队列信息：" + JSON.toJSONString(cMessage));
            capitalRiskProducer.sendCapitalMsg(cMessage);
        } catch (Exception ex) {
            logger.error("发送资金风控队列异常：" + ex.getMessage());
            return ResponseData.error("发送资金风控队列异常");
        }
        return ResponseData.success("提交借款申请成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData sendCapital(OrderMessage message) {
        logger.info("接入资金风控后资产推送的入参为：" + JSON.toJSONString(message));
        String fundCode = message.getFundCode();
        if (StringUtils.isEmpty(fundCode)) {
            return ResponseData.error("订单没有分配渠道");
        }
        OrderInfo orderInfo = null;
        CapitalMessage cMessage = new CapitalMessage();
        if (message.getIfTask() != 1 && message.getChannel() == 1) {
            //return sendCapitalAfterRisk(message);
            cMessage.setProductType("ymt");
            orderInfo = orderContract.getOrderByIbankOrderNo(message.getOrderNo());
            ResponseData<OrderInfo> orderInfoY = orderContract.getOrderByOrderNo(orderInfo.getOrderNo());
            orderInfo = orderInfoY.getData();
        } else {
            cMessage.setProductType("nyd");
            ResponseData<OrderInfo> orderInfoN = orderContract.getOrderByOrderNo(message.getOrderNo());
            orderInfo = orderInfoN.getData();
        }
        if (orderInfo == null) {
            return ResponseData.error("没有查询到订单信息");
        }
        if ("kdlc".equals(fundCode) && redisTemplate.hasKey(OpenPageConstant.NEW_POCKET_DEPOSITORY)) {
            fundCode = "dld";
            orderInfo.setFundCode(fundCode);
            orderContract.updateOrderInfo(orderInfo);
        }
        ResponseData<ChannelProportionConfig> rChannel = orderChannelContract.getChannelByCode(fundCode);
        ChannelProportionConfig channel = rChannel.getData();
        //判断订单渠道是否需要资金风控，不需要直接推送资产，否则放入资金风控队列
        if (channel.getIfRisk().equals(0)) {
            return sendCapitalAfterRisk(message);
        }
        cMessage.setBorrowType("0");
        cMessage.setOrderNo(message.getOrderNo());
        cMessage.setUserId(message.getUserId());

        try {
            //更新订单是否推送资金风控的状态
            orderInfo.setIfRisk(1);
            orderContract.updateOrderInfo(orderInfo);
            //放入资金风控队列
            logger.info("推送资金风控队列信息：" + JSON.toJSONString(cMessage));
            capitalRiskProducer.sendCapitalMsg(cMessage);
        } catch (Exception ex) {
            logger.error("发送资金风控队列异常：" + ex.getMessage());
            return ResponseData.error("发送资金风控队列异常");
        }
        return ResponseData.success("提交借款申请成功！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData sendCapitalAfterRisk(OrderMessage message) {
        try {
            String userId = message.getUserId();
            String orderNo = message.getOrderNo();

            logger.info("资产推送的入参为：" + JSON.toJSONString(message));

            try {
                redisUtil.lock("capital", 1);
                if (redisTemplate.hasKey(Constants.CAPITAL_KEY + userId)) {
                    logger.error("正在处理中,请勿重复点击" + JSON.toJSONString(userId));
                    return ResponseData.error("正在处理中,请勿重复点击");
                } else {
                    redisTemplate.opsForValue().set(Constants.CAPITAL_KEY + userId, "1", 60, TimeUnit.SECONDS);
                    redisUtil.unlock("capital");
                }
            } catch (Exception e) {
                logger.error("写redis出错", e);
                redisUtil.unlock("capital");
                return ResponseData.error("签约失败");
            }
            ResponseData<OrderInfo> orderByOrderNo = null;
            if (message.getIfTask() != 1 && message.getChannel() == 1) {
                OrderInfo orderInfo = orderContract.getOrderByIbankOrderNo(orderNo);
                logger.info("银码头的查出的订单为:" + orderInfo);
                String ibankOrderNo = orderInfo.getOrderNo();
                userId = orderInfo.getUserId();
                orderByOrderNo = orderContract.getOrderByOrderNo(ibankOrderNo);
                if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                    logger.error("查询订单错误");
                    return ResponseData.error("查询订单错误");
                }
                try {
                    orderContractYmt.updateOrderDetailStatus(orderNo, 30);
                } catch (Exception e) {
                    throw e;
                }
            } else {
                orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
                if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                    logger.error("查询订单错误");
                    return ResponseData.error("查询订单错误");
                }
            }

            OrderInfo orderInfo = orderByOrderNo.getData();
            logger.info("资产推送订单信息：" + JSON.toJSONString(orderInfo));
            if (!orderInfo.getOrderStatus().equals(30) || !orderInfo.getAuditStatus().equals("1")) {
                logger.info("资产推送订单状态校验不通过orderNo：" + orderInfo.getOrderNo());
                return ResponseData.error("审核拒绝！");
            }
            if (orderInfo.getIfSign().equals(1)) {
                logger.info("订单可能重复发送orderNo：" + orderInfo.getOrderNo());
                return ResponseData.error("请求失败！");
            }
            //判断时间区间是否为有效时间段
            if ("kdlc".equals(orderInfo.getFundCode()) && DateUtil.isEffectiveDate(DateUtil.dateToHms(new Date()), "23:00:00", "23:59:59")) {
                logger.info("当前时间不在口袋理财放款时间段，订单渠道更新为：dld");
                orderInfo.setFundCode("dld");
                message.setFundCode("dld");
            }
            try {
                //更改订单签约状态
                orderInfo.setIfSign(1);
                orderContract.updateOrderInfo(orderInfo);
            } catch (Exception e) {
                logger.error("更改订单状态时发生异常");
                throw e;
            }
            SendCapitalRunnable runnable = new SendCapitalRunnable(message, fundFactory, orderContract, orderContractYmt);
            threadPoolTaskExecutor.execute(runnable);
            return ResponseData.success();
        } catch (Exception e) {
            logger.error("资产推送时发生异常");
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData reSendCapitalAfterRiskWithCheck(OrderMessage message) {
        try {
            String userId = message.getUserId();
            String orderNo = message.getOrderNo();

            logger.info("资产推送的入参为：" + JSON.toJSONString(message));

            try {
                redisUtil.lock("capital", 1);
                if (redisTemplate.hasKey(Constants.CAPITAL_KEY + userId)) {
                    logger.error("正在处理中,请勿重复点击" + JSON.toJSONString(userId));
                    return ResponseData.error("正在处理中,请勿重复点击");
                } else {
                    redisTemplate.opsForValue().set(Constants.CAPITAL_KEY + userId, "1", 60, TimeUnit.SECONDS);
                    redisUtil.unlock("capital");
                }
            } catch (Exception e) {
                logger.error("写redis出错", e);
                redisUtil.unlock("capital");
                return ResponseData.error("签约失败");
            }
            ResponseData<OrderInfo> orderByOrderNo = null;
            if (message.getIfTask() != 1 && message.getChannel() == 1) {
                OrderInfo orderInfo = orderContract.getOrderByIbankOrderNo(orderNo);
                logger.info("银码头的查出的订单为:" + orderInfo);
                String ibankOrderNo = orderInfo.getOrderNo();
                userId = orderInfo.getUserId();
                orderByOrderNo = orderContract.getOrderByOrderNo(ibankOrderNo);
                if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                    logger.error("查询订单错误");
                    return ResponseData.error("查询订单错误");
                }
                try {
                    orderContractYmt.updateOrderDetailStatus(orderNo, 30);
                } catch (Exception e) {
                    throw e;
                }
            } else {
                orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
                if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                    logger.error("查询订单错误");
                    return ResponseData.error("查询订单错误");
                }
            }
            OrderInfo orderInfo = orderByOrderNo.getData();
            logger.info("资产推送订单信息：" + JSON.toJSONString(orderInfo));
            if (!orderInfo.getOrderStatus().equals(40) && !orderInfo.getOrderStatus().equals(30)) {
                logger.info("资产推送订单状态校验不通过orderNo：" + orderInfo.getOrderNo());
                return ResponseData.error("审核拒绝！");
            }
            if (!orderInfo.getAuditStatus().equals("1")) {
                logger.info("资产推送订单状态校验不通过orderNo：" + orderInfo.getOrderNo());
                return ResponseData.error("审核拒绝！");
            }
             /*if(orderInfo.getIfSign().equals(1) && !orderInfo.getOrderStatus().equals(40)) {
            	logger.info("订单可能重复发送orderNo：" + orderInfo.getOrderNo());
              	return ResponseData.error("请求失败！");
             }*/
             //判断时间区间是否为有效时间段
             if("kdlc".equals(orderInfo.getFundCode()) && DateUtil.isEffectiveDate(DateUtil.dateToHms(new Date()), "23:00:00", "23:59:59")) {
             	logger.info("当前时间不在口袋理财放款时间段，订单渠道更新为：dld");
             	orderInfo.setFundCode("dld");
             	message.setFundCode("dld");
             }
             if("kdlc".equals(orderInfo.getFundCode()) && orderInfo.getBankName() != null && orderInfo.getBankName().indexOf("招商") >= 0) {
            	 orderInfo.setFundCode("dld");
              	message.setFundCode("dld");
 	         }
             try {
            	 if("kdlc".equals(orderInfo.getFundCode())){
	            	 ResponseData<PockerOrderEntity> responseP = orderContract.getByUserId(orderInfo.getOrderNo());
	            	 if(responseP != null && responseP.getData() != null) {
	            		 PockerOrderEntity p = responseP.getData();
	            		 p.setDeleteFlag(1);
	            		 orderContract.updatePockerOrderEntity(p);
	            	 }
            	 }
            	 //更改订单签约状态
                 orderInfo.setIfSign(1);
                 orderInfo.setFundCode(message.getFundCode());
                 orderInfo.setOrderStatus(30);
                 orderContract.updateOrderInfo(orderInfo);
             } catch (Exception e) {
                 logger.error("更改订单状态时发生异常");
                 throw e;
             }
             SendCapitalRunnable runnable = new SendCapitalRunnable(message, fundFactory, orderContract, orderContractYmt);
             threadPoolTaskExecutor.execute(runnable);
             return ResponseData.success();
         } catch (Exception e) {
             logger.error("资产推送时发生异常");
             throw e;
         }
    }

    @Override
    public ResponseData queryLoan(String fundCode) {
        logger.info("查询" + fundCode + "所有待放款的订单");
        //查询所有待放款的订单
        ResponseData responseData = orderContract.queryOrdersWhenOrderStatusIsWait(fundCode);
        if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
            return ResponseData.error("查询" + fundCode + "所有待放款的订单出错");
        }
        List<OrderInfo> list = (List<OrderInfo>) responseData.getData();
        if (list.size() == 0) {
            return ResponseData.success();
        }
        OrderMessage orderMessage = new OrderMessage();
        if ("qcgz".equals(fundCode)) {
            QueryLoanApplyResultRequest queryLoanApplyResultRequest = new QueryLoanApplyResultRequest();
            for (OrderInfo orderInfo : list) {
                ResponseData<JudgeInfo> judgeInfoResponseData = orderContract.judgeBorrow(orderInfo.getUserId(), true);
                if (OpenPageConstant.STATUS_ONE.equals(judgeInfoResponseData.getStatus())) {
                    logger.error("查询用户放款资格发生错误");
                    continue;
                }
                JudgeInfo data = judgeInfoResponseData.getData();
                if ("1".equals(data.getWhetherLoan())) {
                    orderInfo.setOrderStatus(40);
                    //生成异常订单记录
                    try {
                        orderExceptionContract.saveByOrderInfo(orderInfo);
                    } catch (Exception e) {
                        logger.error("生成异常订单信息异常：" + e.getMessage());
                    }
                    orderContract.updateOrderInfo(orderInfo);
                    logger.error("该用户不能继续放款，更改该笔订单状态为放款失败,orderInfo is:" + JSON.toJSONString(orderInfo));
                    continue;
                }
                //查询七彩资产ID
                ResponseData<OrderQcgzDto> orderQcgzDtoResponseData = capitalOrderRelationContract.selectAssetNo(orderInfo.getOrderNo());
                if (OpenPageConstant.STATUS_ONE.equals(orderQcgzDtoResponseData.getStatus())) {
                    logger.error("查询资产号出错,orderInfo is " + JSON.toJSONString(orderInfo));
                    continue;
                }
                OrderQcgzDto qcgzDto = orderQcgzDtoResponseData.getData();
                boolean b = sendForOrder(orderMessage, orderInfo, qcgzDto == null, qcgzDto);
                if (b) {
                    continue;
                }
                //七彩格子的暂时不处理
                continue;
                //防止通知中已经处理
//                try {
//                    if (redisTemplate.hasKey(Constants.CAPITAL_QUERY_KEY + orderInfo.getAssetNo())) {
//                        logger.error("通知中已经处理" + JSON.toJSONString(orderInfo));
//                        continue;
//                    } else {
//                        redisTemplate.opsForValue().set(Constants.CAPITAL_QUERY_KEY + orderInfo.getAssetNo(), "1");
//                    }
//                } catch (Exception e) {
//                    logger.error("写redis出错" ,e);
//                }

//                String assetNo = qcgzDto.getAssetNo();
//                queryLoanApplyResultRequest.setAssetId(assetNo);
//                ResponseData applyResult = qcgzService.queryLoanApplyResult(queryLoanApplyResultRequest);
//                if (OpenPageConstant.STATUS_ONE.equals(applyResult.getStatus())){
//                    logger.error("查询七彩格子放款情况发生错误,assetId is"+assetNo);
//                    continue;
//                }
//                QueryLoanApplyResultResponse.Datas  applyResultData = (QueryLoanApplyResultResponse.Datas) applyResult.getData();
//                if (applyResultData.getLoanStatus() == 1){
//                    //处理中，不做处理
//                    continue;
//                }else  if (applyResultData.getLoanStatus() == 2){
//                    //放款成功
//                    //qcgzBusiness.
//                }else if (applyResultData.getLoanStatus() == 3){
//                    //放款失败
//                }else {
//                    continue;
//                }
            }
        } else if ("dld".equals(fundCode)) {
            UserDld userDld = null;
            RemitMessage remitMessage = new RemitMessage();
            RemitInfo remitInfo = new RemitInfo();
            //处理多来点待放款问题
            for (OrderInfo orderInfo : list) {
                ResponseData<JudgeInfo> judgeInfoResponseData = orderContract.judgeBorrow(orderInfo.getUserId(), true);
                if (OpenPageConstant.STATUS_ONE.equals(judgeInfoResponseData.getStatus())) {
                    logger.error("查询用户放款资格发生错误");
                    continue;
                }
                JudgeInfo data = judgeInfoResponseData.getData();
                if ("1".equals(data.getWhetherLoan())) {
                    orderInfo.setOrderStatus(40);
                    //生成异常订单记录
                    orderExceptionContract.saveByOrderInfo(orderInfo);
                    orderContract.updateOrderInfo(orderInfo);
                    logger.error("该用户不能继续放款，更改该笔订单状态为放款失败,orderInfo is:" + JSON.toJSONString(orderInfo));
                    continue;
                }
                try {
                    userDld = userDldService.getUserDldByUserId(orderInfo.getUserId());
                } catch (Exception e) {
                    logger.error("查询多来点用户信息出错,userId is " + orderInfo.getUserId());
                    continue;
                }
                boolean b = sendForOrder(orderMessage, orderInfo, userDld == null, userDld);
                if (b) {
                    continue;
                }
                //防止通知中已经处理
                try {
                    if (redisTemplate.hasKey(Constants.CAPITAL_QUERY_KEY + orderInfo.getOrderNo())) {
                        logger.error("通知中已经处理" + JSON.toJSONString(orderInfo));
                        continue;
                    } else {
                        redisTemplate.opsForValue().set(Constants.CAPITAL_QUERY_KEY + orderInfo.getOrderNo(), "1",24*60,TimeUnit.MINUTES);
                    }
                } catch (Exception e) {
                    logger.error("写redis出错", e);
                    continue;
                }
                ResponseData orderQuery = dldService.loanOrderQueryByOrderNo(orderInfo.getOrderNo());
                if (OpenPageConstant.STATUS_ONE.equals(orderQuery.getStatus())) {
                    logger.error("查询多来点放款情况失败,orderInfo is " + JSON.toJSONString(orderInfo));
                    continue;
                }
                String dataData = (String) orderQuery.getData();
                JSONObject jsonObj = JSON.parseObject(dataData);
                JSONObject jsonObject = jsonObj.getJSONObject("data");
                if (jsonObject == null) {
                    orderInfo.setOrderStatus(40);
                    //生成异常订单记录
                    orderExceptionContract.saveByOrderInfo(orderInfo);
                    orderContract.updateOrderInfo(orderInfo);
                    continue;
                }
                String respCode = jsonObject.getString("respCode");
                if ("TS1001".equals(respCode)) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String lentTime = jsonObject1.getString("lentTime");
                    Date date = DateUtil.strToDate(lentTime);
                    //放款成功,通知zues
                    remitMessage.setRemitStatus("0");
                    remitMessage.setRemitAmount(orderInfo.getLoanAmount());
                    remitMessage.setOrderNo(orderInfo.getOrderNo());
                    remitMessage.setRemitTime(date);
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
                    remitInfo.setRemitTime(date);
                    remitInfo.setOrderNo(orderInfo.getOrderNo());
                    remitInfo.setRemitStatus("0");
                    remitInfo.setFundCode("dld");
                    remitInfo.setChannel(orderInfo.getChannel());
                    remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                    logger.info("多来点放款流水:" + JSON.toJSON(remitInfo));
                    try {
                        rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                        logger.info("多来点放款流水发送mq成功");
                    } catch (Exception e) {
                        logger.error("发送mq消息发生异常");
                        DingdingUtil.getErrMsg("多来点放款查询成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(orderInfo));
                    }
                }
            }

        } else if (FundSourceEnum.KDLC.getCode().equals(fundCode)) {
            //跑批处理口袋的放款
            for (OrderInfo orderInfo : list) {
                //防止通知中已经处理
                ResponseData<PockerOrderEntity> pocketOrderByOrderNo = orderContract.getByUserId(orderInfo.getOrderNo());
                if (OpenPageConstant.STATUS_ONE.equals(pocketOrderByOrderNo.getStatus())) {
                    continue;
                }
                PockerOrderEntity entity = pocketOrderByOrderNo.getData();
                try {
                    if (redisTemplate.hasKey(Constants.CAPITAL_QUERY_KEY + entity.getPayOrderId())) {
                        logger.error("通知中已经处理" + JSON.toJSONString(orderInfo));
                        continue;
                    } else {
                        redisTemplate.opsForValue().set(Constants.CAPITAL_QUERY_KEY + entity.getPayOrderId(), "1",24*60,TimeUnit.MINUTES);
                    }
                } catch (Exception e) {
                    logger.error("写redis出错", e);
                    continue;
                }
                ResponseData orderQuery = pocketService.queryOrder(orderInfo);
                if (OpenPageConstant.STATUS_ONE.equals(orderQuery.getStatus())) {
                    logger.error("查询口袋理财放款情况失败,orderInfo is " + JSON.toJSONString(orderInfo));
                    continue;
                }
                Map<String, String> parse = (Map<String, String>) orderQuery.getData();
                String status = parse.get("status");
                if ("21".equals(status)) {
                    String loanTime = parse.get("loan_time");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = dateFormat.parse(loanTime);
                    } catch (Exception e) {
                        continue;
                    }
                    //处理放款
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
                        logger.info("放款成功发送ibank." + remitMessage.toString());
                        rabbitmqProducerProxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
                    }

                    RemitInfo remitInfo = new RemitInfo();

                    remitInfo.setRemitTime(date);
                    remitInfo.setOrderNo(orderInfo.getOrderNo());
                    remitInfo.setRemitStatus("0");
                    remitInfo.setFundCode(FundSourceEnum.KDLC.getCode());
                    remitInfo.setChannel(orderInfo.getChannel());
                    remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                    logger.info("放款流水:" + remitInfo.toString());
                    try {
                        rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                        logger.info("放款流水发送mq成功");
                    } catch (Exception e) {
                        logger.error("发送mq消息发生异常");
                    }
                    try {
                        orderContract.updatePockerOrderEntity(entity);
                    } catch (Exception ex) {
                        logger.error("更新订单状态失败：" + ex.getMessage());
                    }
                }
            }
        }
        return ResponseData.success();
    }

    private boolean sendForOrder(OrderMessage orderMessage, OrderInfo orderInfo, boolean b, Object userDld) {
        if (b) {
            //查询不到需要重新注册
            orderMessage.setUserId(orderInfo.getUserId());
            orderMessage.setOrderNo(orderInfo.getOrderNo());
            orderMessage.setChannel(orderInfo.getChannel());
            orderMessage.setFundCode(orderInfo.getFundCode());
            orderMessage.setIfTask(1);
            sendCapital(orderMessage);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断时间是否在时间段内
     *
     * @return
     */
    public static boolean belongCalendar() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date beginTime = dateFormat.parse("05:00:00");
            Date endTime = dateFormat.parse("22:00:00");
            Date nowTime = new Date();
            String format = dateFormat.format(nowTime);
            nowTime = dateFormat.parse(format);

            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);

            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);

            Calendar end = Calendar.getInstance();
            end.setTime(endTime);

            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }


    @Override
    public BigDecimal queryDldUserBalance() {
        return dldService.userBalanceQuery();
    }

    @Override
    public ResponseData withholdTask() {
        logger.info("开始放款成功后的代扣跑批");
        ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.MIN_1.getCode());
        if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
            logger.error("查询开始时间失败");
            return ResponseData.error("查询开始时间失败");
        }
        WithholdTaskConfig config = responseData.getData();
        Date startTime = config.getStartTime();
        logger.info("开始时间为" + JSON.toJSONString(startTime));
        Date date = new Date();
        logger.info("截止时间为:" + JSON.toJSONString(date));
        try {
            ResponseData<List<RemitInfo>> remitResult = remitContract.getSuccessRemit(startTime, date);
            if (OpenPageConstant.STATUS_ONE.equals(remitResult.getStatus())) {
                return ResponseData.error("代扣跑批失败");
            }
            List<RemitInfo> models = remitResult.getData();
            if (models != null && models.size() > 0) {
                logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
                for (RemitInfo model : models) {
                    String orderNo = model.getOrderNo();
                    ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
                    if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                        continue;
                    }
                    OrderInfo info = orderByOrderNo.getData();
                    ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(info.getMemberId());
                    if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
                        continue;
                    }
                    WithholdOrderInfo data = withholdOrderByMemberId.getData();
                    if (data != null) {
                    	if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
                        //调用代扣接口
                        RequestData requestData = new RequestData();
                        String url = dldConfig.getCommonPayUrl();
                        SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
                        submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
                        submitWithholdDto.setWithholdAmount(Double.valueOf(info.getMemberFee().toString()));
                        requestData.setData(submitWithholdDto);
                        requestData.setRequestAppId("nyd");
                        requestData.setRequestId(UUID.randomUUID().toString());
                        requestData.setRequestTime(JSON.toJSONString(new Date()));
                        String json = JSON.toJSONString(requestData);
                        try {
                            String sendPost = HttpUtil.sendPost(url, json);
                        } catch (Exception e) {
                            logger.error("调用生成代扣订单接口发生异常");
                        }
                    }
                }
            }
            orderContract.updateTaskTimeByCode(date, WithholdConfigCode.MIN_1.getCode());
        } catch (Exception e) {
            logger.error("代扣跑批失败", e);
            return ResponseData.error("代扣跑批失败");
        }
        logger.info("结束放款成功后的代扣跑批");
        return ResponseData.success();
    }

    @Override
    public ResponseData withholdRefusedTask() {
    	logger.info("开始拒绝放款后的代扣跑批");
    	ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.MIN_FAIL_1.getCode());
    	if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
    		logger.error("查询开始时间失败");
    		return ResponseData.error("查询开始时间失败");
    	}
    	WithholdTaskConfig config = responseData.getData();
    	Date startTime = config.getStartTime();
    	logger.info("开始时间为"+JSON.toJSONString(startTime));
    	Date date = new Date();
    	logger.info("截止时间为:"+JSON.toJSONString(date));
    	try {
    		ResponseData<List<OrderInfo>> orderResult = orderContract.getRefusedOrders(startTime, date);
    		if (OpenPageConstant.STATUS_ONE.equals(orderResult.getStatus())) {
    			return ResponseData.error("代扣跑批失败");
    		}
    		List<OrderInfo> models = orderResult.getData();
    		if (models != null && models.size() > 0) {
    			logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
    			for (OrderInfo model : models) {
    				ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(model.getMemberId());
    				if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
    					continue;
    				}
    				WithholdOrderInfo data = withholdOrderByMemberId.getData();
    				if (data != null) {
    					if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
    					//调用代扣接口
    					RequestData requestData = new RequestData();
    					String url = dldConfig.getCommonPayUrl();
    					SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
    					submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
    					submitWithholdDto.setWithholdAmount(Double.valueOf(50));
    					requestData.setData(submitWithholdDto);
    					requestData.setRequestAppId("nyd");
    					requestData.setRequestId(UUID.randomUUID().toString());
    					requestData.setRequestTime(JSON.toJSONString(new Date()));
    					String json = JSON.toJSONString(requestData);
    					try {
    						String sendPost = HttpUtil.sendPost(url, json);
    					} catch (Exception e) {
    						logger.error("调用生成代扣订单接口发生异常");
    					}
    				}
    			}
    		}
    		orderContract.updateTaskTimeByCode(date,WithholdConfigCode.MIN_FAIL_1.getCode());
    	} catch (Exception e) {
    		logger.error("代扣跑批失败",e);
    		return ResponseData.error("代扣跑批失败");
    	}
    	logger.info("开始拒绝放款后的代扣跑批");
    	return ResponseData.success();
    }
    @Override
    public ResponseData withholdTask10Min() {
    	logger.info("开始放款成功10min后的代扣跑批");
    	ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.MIN_10.getCode());
    	if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
    		logger.error("查询开始时间失败");
    		return ResponseData.error("查询开始时间失败");
    	}
    	WithholdTaskConfig config = responseData.getData();
    	Date startTime = config.getStartTime();
    	startTime = DateUtils.addMinutes(startTime, -10);
    	logger.info("开始时间为"+JSON.toJSONString(startTime));
    	Date date = new Date();
    	Date endDate = DateUtils.addMinutes(date, -10);
    	logger.info("截止时间为:"+JSON.toJSONString(date));
    	try {
    		ResponseData<List<RemitInfo>> remitResult = remitContract.getSuccessRemit(startTime, endDate);
    		if (OpenPageConstant.STATUS_ONE.equals(remitResult.getStatus())) {
    			return ResponseData.error("代扣跑批失败");
    		}
    		List<RemitInfo> models = remitResult.getData();
    		if (models != null && models.size() > 0) {
    			logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
    			for (RemitInfo model : models) {
    				String orderNo = model.getOrderNo();
    				ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
    				if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
    					continue;
    				}
    				OrderInfo info = orderByOrderNo.getData();
    				ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(info.getMemberId());
    				if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
    					continue;
    				}
    				WithholdOrderInfo data = withholdOrderByMemberId.getData();
    				if (data != null) {
    					if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
    					//调用代扣接口
    					RequestData requestData = new RequestData();
    					String url = dldConfig.getCommonPayUrl();
    					SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
    					submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
    					submitWithholdDto.setWithholdAmount(Double.valueOf(info.getMemberFee().toString()));
    					requestData.setData(submitWithholdDto);
    					requestData.setRequestAppId("nyd");
    					requestData.setRequestId(UUID.randomUUID().toString());
    					requestData.setRequestTime(JSON.toJSONString(new Date()));
    					String json = JSON.toJSONString(requestData);
    					try {
    						String sendPost = HttpUtil.sendPost(url, json);
    					} catch (Exception e) {
    						logger.error("调用生成代扣订单接口发生异常");
    					}
    				}
    			}
    		}
    		orderContract.updateTaskTimeByCode(date,WithholdConfigCode.MIN_10.getCode());
    	} catch (Exception e) {
    		logger.error("代扣跑批失败",e);
    		return ResponseData.error("代扣跑批失败");
    	}
    	logger.info("*****结束放款成功10min后的代扣跑批**********");
    	return ResponseData.success();
    }
    @Override
    public ResponseData withholdRefusedTask10Min() {
    	logger.info("开始放款拒绝后10min后的代扣跑批");
    	ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.MIN_FAIL_10.getCode());
    	if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
    		logger.error("查询开始时间失败");
    		return ResponseData.error("查询开始时间失败");
    	}
    	WithholdTaskConfig config = responseData.getData();
    	Date startTime = config.getStartTime();
    	startTime = DateUtils.addMinutes(startTime, -10);
    	logger.info("开始时间为"+JSON.toJSONString(startTime));
    	Date date = new Date();
    	Date endDate = DateUtils.addMinutes(date, -10);
    	logger.info("截止时间为:"+JSON.toJSONString(date));
    	try {
    		ResponseData<List<OrderInfo>> orderResult = orderContract.getRefusedOrders(startTime, endDate);
    		if (OpenPageConstant.STATUS_ONE.equals(orderResult.getStatus())) {
    			return ResponseData.error("代扣跑批失败");
    		}
    		List<OrderInfo> models = orderResult.getData();
    		if (models != null && models.size() > 0) {
    			logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
    			for (OrderInfo model : models) {
    				ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(model.getMemberId());
    				if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
    					continue;
    				}
    				WithholdOrderInfo data = withholdOrderByMemberId.getData();
    				if (data != null) {
    					if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
    					//调用代扣接口
    					RequestData requestData = new RequestData();
    					String url = dldConfig.getCommonPayUrl();
    					SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
    					submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
    					submitWithholdDto.setWithholdAmount(Double.valueOf(50));
    					requestData.setData(submitWithholdDto);
    					requestData.setRequestAppId("nyd");
    					requestData.setRequestId(UUID.randomUUID().toString());
    					requestData.setRequestTime(JSON.toJSONString(new Date()));
    					String json = JSON.toJSONString(requestData);
    					try {
    						String sendPost = HttpUtil.sendPost(url, json);
    					} catch (Exception e) {
    						logger.error("调用生成代扣订单接口发生异常");
    					}
    				}
    			}
    		}
    		orderContract.updateTaskTimeByCode(date,WithholdConfigCode.MIN_FAIL_10.getCode());
    	} catch (Exception e) {
    		logger.error("代扣跑批失败",e);
    		return ResponseData.error("代扣跑批失败");
    	}
    	logger.info("*****结束放款拒绝后10min后的代扣跑批**********");
    	return ResponseData.success();
    }

    public static void main(String[] args) {
        String string = (new BigDecimal(0.05).multiply(new BigDecimal(14)).setScale(1, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(1000)).setScale(0,BigDecimal.ROUND_DOWN)).toPlainString();
        System.out.println(string);
    }

    @Override
    public ResponseData pushPocketOrder(String order) {
        logger.info("begin to push pocket order,and orderNo is:" + order);
        ResponseData responseData = ResponseData.success();
        ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(order);
        if (!OpenPageConstant.STATUS_ZERO.equals(orderByOrderNo.getStatus())) {
            return ResponseData.error("select order by orderNo error and orderNo is" + order);
        }
        OrderInfo orderInfo = orderByOrderNo.getData();
        try {
            ResponseData<UserInfo> userInfoResponseData = userIdentityContract.getUserInfo(orderInfo.getUserId());
            if (!OpenPageConstant.STATUS_ZERO.equals(userInfoResponseData.getStatus())) {
                return ResponseData.error("select user by userId error and userId is" + orderInfo.getUserId());
            }
            UserInfo userInfo = userInfoResponseData.getData();
            PocketCreateOrderLendPayDto dto = new PocketCreateOrderLendPayDto();
            PocketUserBase2 userBase = new PocketUserBase2();
            userBase.setIdNumber(userInfo.getIdNumber());
            PocketOrderBase2 orderBase = new PocketOrderBase2();
            orderBase.setApr("18");
            orderBase.setCounterFee("0");
            orderBase.setLendPayType("1");
            orderBase.setLoanMethod("0");
            orderBase.setLoanTerm(orderInfo.getBorrowTime().toString());
            orderBase.setMoneyAmount((orderInfo.getLoanAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
            orderBase.setOrderTime(String.valueOf(System.currentTimeMillis() / 1000));
            orderBase.setLoanInterests((new BigDecimal(0.05).multiply(new BigDecimal(orderInfo.getBorrowTime()).setScale(1, BigDecimal.ROUND_DOWN)).multiply(orderInfo.getLoanAmount()).setScale(0,BigDecimal.ROUND_DOWN)).toPlainString());
            orderBase.setOutTradeNo(orderInfo.getOrderNo());
            orderBase.setUsageOfLoan("5");
            dto.setOrderBase(orderBase);
            dto.setUserBase(userBase);
            ResponseData<PocketParentResult> orderLendPay = pocket2Service.createOrderLendPay(dto);
            logger.info("createOrderLendPay result is:" + ToStringBuilder.reflectionToString(orderLendPay));
            UserPocket userPocket = new UserPocket();
            userPocket.setUserId(orderInfo.getUserId());
            if (!OpenPageConstant.STATUS_ZERO.equals(orderLendPay.getStatus())) {
                //推单失败
                userPocket.setStage(Integer.valueOf(PocketAccountEnum.Not_Accepted.getCode()));
                userPocketService.update(userPocket);
                orderHandler.orderFailHankdler(orderInfo, Constants.KDLC_CALLBACK_PREFIX, orderLendPay);
                return ResponseData.error(orderLendPay.getMsg());
            }
            PocketParentResult payData = orderLendPay.getData();
            if (!OpenPageConstant.STATUS_ZERO.equals(payData.getRetCode())) {
                //推单失败
                userPocket.setStage(Integer.valueOf(PocketAccountEnum.Not_Accepted.getCode()));
                userPocketService.update(userPocket);
                orderHandler.orderFailHankdler(orderInfo, Constants.KDLC_CALLBACK_PREFIX, orderLendPay);
                if ("50013".equals(payData.getRetCode())) {
                    Calendar curDate = Calendar.getInstance();
                    Calendar tommorowDate = new GregorianCalendar(curDate
                            .get(Calendar.YEAR), curDate.get(Calendar.MONTH),
                            curDate.get(Calendar.DATE) + 1, 0, 0, 0);
                    long time = (tommorowDate.getTimeInMillis() - curDate
                            .getTimeInMillis()) / 1000;
                    redisTemplate.opsForValue().set(OpenPageConstant.NEW_POCKET_DEPOSITORY, "1",time, TimeUnit.SECONDS);
                }
                return ResponseData.error(orderLendPay.getMsg());
            }
            String retData = payData.getRetData();
            PocketOrderVo orderVo = JSONObject.parseObject(retData, PocketOrderVo.class);
            BalanceOrder balanceOrder = new BalanceOrder();
            balanceOrder.setAssetNo(orderVo.getOrder_id());
            balanceOrder.setFundCode(FundSourceEnum.KDLC.getCode());
            balanceOrder.setIfSuccess(0);
            balanceOrder.setMobile(userInfo.getAccountNumber());
            balanceOrder.setName(userInfo.getRealName());
            balanceOrder.setOrderNo(orderInfo.getOrderNo());
            balanceOrder.setLoanTime(orderInfo.getLoanTime());
            capitalOrderRelationContract.saveBalanceOrder(balanceOrder);
            //更新口袋理财用户表
            userPocket.setStage(Integer.valueOf(PocketAccountEnum.Have_Accepted.getCode()));
            userPocketService.update(userPocket);
        }catch (Exception e){
            logger.error("begin confirm order html,and message is:" + order,e);
            responseData.setMsg("confirm order html has exception");
            responseData.setStatus(OpenPageConstant.STATUS_ONE);
            orderHandler.orderFailHankdler(orderInfo, Constants.KDLC_CALLBACK_PREFIX);
            redisTemplate.delete("pocket:complianceBorrow" + order);
            return responseData;
        }
        return responseData;
    }

    @Override
    public ResponseData withholdTaskRetry1Hour() {
        logger.info("*******开始放款成功1h后的代扣跑批**********");
        ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.HOUR_1.getCode());
        if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
            logger.error("查询开始时间失败");
            return ResponseData.error("查询开始时间失败");
        }
        WithholdTaskConfig config = responseData.getData();
        Date startTime = config.getStartTime();
        startTime = DateUtils.addHours(startTime, -1);
        logger.info("开始时间为" + JSON.toJSONString(startTime));
        Date date = new Date();
        Date endDate = DateUtils.addHours(date, -1);
        logger.info("截止时间为:" + JSON.toJSONString(date));
        try {
            ResponseData<List<RemitInfo>> remitResult = remitContract.getSuccessRemit(startTime, endDate);
            if (OpenPageConstant.STATUS_ONE.equals(remitResult.getStatus())) {
                return ResponseData.error("代扣跑批失败");
            }
            List<RemitInfo> models = remitResult.getData();
            if (models != null && models.size() > 0) {
                logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
                for (RemitInfo model : models) {
                    String orderNo = model.getOrderNo();
                    ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
                    if (OpenPageConstant.STATUS_ONE.equals(orderByOrderNo.getStatus())) {
                        continue;
                    }
                    OrderInfo info = orderByOrderNo.getData();
                    ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(info.getMemberId());
                    if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
                        continue;
                    }
                    WithholdOrderInfo data = withholdOrderByMemberId.getData();
                    if (data != null) {
                    	if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
                        //调用代扣接口
                        RequestData requestData = new RequestData();
                        String url = dldConfig.getCommonPayUrl();
                        SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
                        submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
                        submitWithholdDto.setWithholdAmount(Double.valueOf(info.getMemberFee().toString()));
                        requestData.setData(submitWithholdDto);
                        requestData.setRequestAppId("nyd");
                        requestData.setRequestId(UUID.randomUUID().toString());
                        requestData.setRequestTime(JSON.toJSONString(new Date()));
                        String json = JSON.toJSONString(requestData);
                        try {
                            String sendPost = HttpUtil.sendPost(url, json);
                        } catch (Exception e) {
                            logger.error("调用生成代扣订单接口发生异常");
                        }
                    }
                }
            }
            orderContract.updateTaskTimeByCode(date, WithholdConfigCode.HOUR_1.getCode());
        } catch (Exception e) {
            logger.error("代扣跑批失败", e);
            return ResponseData.error("代扣跑批失败");
        }
        logger.info("*****结束放款成功1h后的代扣跑批**********");
        return ResponseData.success();
    }
    @Override
    public ResponseData withholdRefusedTaskRetry1Hour() {
    	logger.info("*******开始放款拒绝1h后的代扣跑批**********");
    	ResponseData<WithholdTaskConfig> responseData = orderContract.selectTaskTimeByCode(WithholdConfigCode.HOUR_FAIL_1.getCode());
    	if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
    		logger.error("查询开始时间失败");
    		return ResponseData.error("查询开始时间失败");
    	}
    	WithholdTaskConfig config = responseData.getData();
    	Date startTime = config.getStartTime();
    	startTime = DateUtils.addHours(startTime, -1);
    	logger.info("开始时间为"+JSON.toJSONString(startTime));
    	Date date = new Date();
    	Date endDate = DateUtils.addHours(date, -1);
    	logger.info("截止时间为:"+JSON.toJSONString(date));
    	try {
    		ResponseData<List<OrderInfo>> orderResult = orderContract.getRefusedOrders(startTime, endDate);
    		if (OpenPageConstant.STATUS_ONE.equals(orderResult.getStatus())) {
    			return ResponseData.error("代扣跑批失败");
    		}
    		List<OrderInfo> models = orderResult.getData();
    		if (models != null && models.size() > 0) {
    			logger.info("查询到的放款记录为,models:" + JSON.toJSONString(models));
    			for (OrderInfo model : models) {
    				ResponseData<WithholdOrderInfo> withholdOrderByMemberId = orderContract.getWithholdOrderByMemberId(model.getMemberId());
    				if (OpenPageConstant.STATUS_ONE.equals(withholdOrderByMemberId.getStatus())) {
    					continue;
    				}
    				WithholdOrderInfo data = withholdOrderByMemberId.getData();
    				if (data != null) {
    					if(data.getOrderStatus().equals(2)) {
                    		continue;
                    	}
    					//调用代扣接口
    					RequestData requestData = new RequestData();
    					String url = dldConfig.getCommonPayUrl();
    					SubmitWithholdDto submitWithholdDto = new SubmitWithholdDto();
    					submitWithholdDto.setPayOrderNo(data.getPayOrderNo());
    					submitWithholdDto.setWithholdAmount(Double.valueOf(50));
    					requestData.setData(submitWithholdDto);
    					requestData.setRequestAppId("nyd");
    					requestData.setRequestId(UUID.randomUUID().toString());
    					requestData.setRequestTime(JSON.toJSONString(new Date()));
    					String json = JSON.toJSONString(requestData);
    					try {
    						String sendPost = HttpUtil.sendPost(url, json);
    					} catch (Exception e) {
    						logger.error("调用生成代扣订单接口发生异常");
    					}
    				}
    			}
    		}
    		orderContract.updateTaskTimeByCode(date,WithholdConfigCode.HOUR_FAIL_1.getCode());
    	} catch (Exception e) {
    		logger.error("代扣跑批失败",e);
    		return ResponseData.error("代扣跑批失败");
    	}
    	logger.info("*****结束放款拒绝1h后的代扣跑批**********");
    	return ResponseData.success();
    }
    
    
    /**
     * 处理待放款跑批记录
     */

    @Override
    public ResponseData queryKdlcWaitLoan(String funCode) {
        logger.info("查询" + funCode + "前十分钟待放款的数据");
        ResponseData data = orderContract.getKdlcWaitLoan(funCode);
        if ("1".equals(data.getStatus())) {
            return ResponseData.error("查询" + funCode + "所有待放款的订单出错");
        }
        List<OrderInfo> orderList = (List<OrderInfo>) data.getData();
        if (orderList == null || orderList.size() == 0) {
            logger.info("没有查到" + funCode + "待放款订单");
            return ResponseData.success();
        }
        if (funCode.equals("kdlc")) {
            for (OrderInfo orderInfo : orderList) {
                ResponseData responseData = pocketService.withdrawQuery(orderInfo.getOrderNo());
                this.handleResult(responseData, orderInfo);

            }
            logger.info("查询" + funCode + "前十分钟待放款的数据结束");
        } else if (funCode.equals("dld")) {
            for (OrderInfo orderInfo : orderList) {
                ResponseData responseData = dldService.loanOrderQueryByOrderNo(orderInfo.getOrderNo());
                handleDldResult(responseData, orderInfo);

            }
        }
        return ResponseData.success();
    }

    /**
     * 处理多来点借款订单查询结果
     *
     * @param responseData
     * @param orderInfo
     */
    private void handleDldResult(ResponseData responseData, OrderInfo orderInfo) {
        if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
            return;
        }
        if (redisTemplate.hasKey(Constants.DLD_CALLBACK_PREFIX + orderInfo.getOrderNo())) {
            logger.error("有重复通知" + JSON.toJSONString(orderInfo.getOrderNo()));
            return;
        } else {
            redisTemplate.opsForValue().set(Constants.DLD_CALLBACK_PREFIX + orderInfo.getOrderNo(), "1",24*60,TimeUnit.MINUTES);
        }
        String dataData = (String) responseData.getData();
        JSONObject data = JSON.parseObject(dataData);
        JSONObject jsonObject = data.getJSONObject("data");
        if (jsonObject == null) {
            orderInfo.setOrderStatus(40);
            //生成异常订单记录
            try {
                orderExceptionContract.saveByOrderInfo(orderInfo);
            } catch (Exception e) {
                logger.error("生成异常订单信息异常：" + e.getMessage());
            }
            orderContract.updateOrderInfo(orderInfo);
        }
        String respCode = jsonObject.getString("respCode");

        if ("TS1001".equals(respCode)) {
            //放款成功,通知zues
            RemitMessage remitMessage = new RemitMessage();
            remitMessage.setRemitStatus("0");
            remitMessage.setRemitAmount(orderInfo.getLoanAmount());
            remitMessage.setOrderNo(orderInfo.getOrderNo());
            remitMessage.setRemitTime(DateUtil.parseStrToDate(JsonUtils.getValue(dataData, 3, new String[]{"data", "data", "lentTime"}).toString(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
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
            remitInfo.setRemitAmount(orderInfo.getLoanAmount());
            remitInfo.setRemitTime(DateUtil.parseStrToDate(JsonUtils.getValue(dataData, 3, new String[]{"data", "data", "lentTime"}).toString(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI));
            logger.info("多来点放款流水:" + JSON.toJSON(remitInfo));
            try {
                rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                logger.info("多来点放款流水发送mq成功");
            } catch (Exception e) {
                logger.error("发送mq消息发生异常");
            }

        } else if ("TS1111".equals(respCode)) {
            redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + orderInfo.getOrderNo());
            return;
        } else {
            orderInfo.setOrderStatus(40);
            //生成异常订单记录
            orderExceptionContract.saveByOrderInfo(orderInfo);
            orderContract.updateOrderInfo(orderInfo);
            redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + orderInfo.getOrderNo());
        }

    }

    private void handleResult(ResponseData data, OrderInfo orderInfo) {
        if ("0".equals(data.getStatus()) && data.getData() != null) {
            Map<String, String> map = (Map<String, String>) data.getData();
            String code = map.get("code");
            //放款通知
            if ("0".equals(code)) {
                if (redisTemplate.hasKey(Constants.KDLC_CALLBACK_PREFIX_NOTICE + orderInfo.getOrderNo())) {
                    logger.error("重复处理kdlc查询借款订单接口");
                    return;
                } else {
                    redisTemplate.opsForValue().set(Constants.KDLC_CALLBACK_PREFIX_NOTICE + orderInfo.getOrderNo(), "1",24*60,TimeUnit.MINUTES);
                }
                try {
                    pocketService.createOrder(orderInfo);
                } catch (Exception e) {
                    logger.error("借款成功后创建口袋理财订单异常：" + e.getMessage());
                }
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
                remitInfo.setFundCode("kdlc");
                remitInfo.setChannel(orderInfo.getChannel());
                remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                logger.info("口袋理财放款流水:" + JSON.toJSON(remitInfo));
                try {
                    rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                    logger.info("口袋理财放款流水发送mq成功");
                } catch (Exception e) {
                    logger.error("发送mq消息发生异常");
                    //DingdingUtil.getErrMsg("口袋理财放款成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(remitInfo));
                }

            }
            if ("1003".equals(code) || "1006".equals(code)) {
                //借款失败
                String reason = orderInfo.getPayFailReason();
                if (reason == null) {
                    reason = "";
                }
                DingdingUtil.getErrMsg("口袋理财发起借款申请失败，订单号为：" + orderInfo.getOrderNo() + reason + "；错误码：" + code + "；msg：" + map.get("msg"));
                orderInfo.setOrderStatus(40);
                //生成异常订单记录
                try {
                    orderExceptionContract.saveByOrderInfo(orderInfo);
                } catch (Exception e) {
                    logger.error("生成异常订单信息异常：" + e.getMessage());
                }
                orderContract.updateOrderInfo(orderInfo);
                redisTemplate.delete(Constants.KDLC_CALLBACK_PREFIX + orderInfo.getOrderNo());
            }

        }

    }

    /**
     * 更新 创建订单处理中 的状态
     */
    @Override
    public void kdlcQueryCreateOrder() {
        logger.info("口袋理财 定时查询  创建订单的状态开始.................");
        List<PockerOrderEntity> pockerOrderEntityList = orderContract.taskCreateStatusAllData();
        if (CollectionUtils.isEmpty(pockerOrderEntityList)) {
            logger.info("未获取到当前 创建订单处理中的 订单数据");
            return;
        }
        //更新创建订单处理中的订单
        for (PockerOrderEntity pockerOrderEntity : pockerOrderEntityList) {
            try {
                logger.info("订单号: {}", pockerOrderEntity.getOrderNo());
                String orderNo = pockerOrderEntity.getOrderNo();
                //订单表信息
                ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
                if (!"0".equals(orderByOrderNo.getStatus()) || null == orderByOrderNo.getData()) {
                    logger.info("未查询到该订单:{}" + pockerOrderEntity.getOrderNo());
                    continue;
                }
                OrderInfo orderInfo = orderByOrderNo.getData();
                //用户表信息
                ResponseData<UserInfo> userInfoResult = userIdentityContract.getUserInfo(orderInfo.getUserId());
                if (!"0".equals(userInfoResult.getStatus()) || null == userInfoResult.getData()) {
                    logger.info("未查询到该userId:{}" + orderInfo.getUserId());
                    continue;
                }
                UserInfo userInfo = userInfoResult.getData();

                Map<String, String> map = new HashMap<>();
                map.put("account", pocketConfig.getPocketOrderAccount());
                map.put("timestamp", String.valueOf(pocketOrderUtil.getSecondTimestamp(new Date())));
                map.put("id_number", userInfo.getIdNumber());
                map.put("out_trade_no", orderNo);
                String sign = pocketOrderUtil.createSign(userInfo.getIdNumber());
                map.put("sign", sign);
                String result = PocketPayUtil.sendPostRequestBody(pocketConfig.getPocketOrderQuery(), JSON.toJSONString(map));
                if (StringUtils.isBlank(result)) {
                    PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
                    pockerOrderEntityUpStatus.setOrderStatus(1);
                    pockerOrderEntityUpStatus.setOrderNo(orderNo);
                    orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                    logger.error("查询订单未收到口袋回复信息,订单号为:{}", orderNo);
                    continue;
                }
                Map<String, String> parse = (Map<String, String>) JSONObject.parse(result);
                logger.info("口袋理财订单查询返回结果：" + parse);
                String code = String.valueOf(parse.get("code"));
                if (!"0".equals(code)) {
                    PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
                    pockerOrderEntityUpStatus.setOrderStatus(1);
                    pockerOrderEntityUpStatus.setOrderNo(orderNo);
                    orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                    continue;
                }
                String status = String.valueOf(parse.get("status"));
                if ("21".equals(status)) {
                    PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
                    //生成订单成功
                    pockerOrderEntityUpStatus.setOrderStatus(2);
                    pockerOrderEntityUpStatus.setOrderNo(orderNo);
                    orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                    continue;
                }
                if ("-30".equals(status) || "-31".equals(status) || "-32".equals(status) || "-20".equals(status) || "-11".equals(status)) {
                    PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
                    //生成订单处理中
                    pockerOrderEntityUpStatus.setOrderStatus(3);
                    pockerOrderEntityUpStatus.setOrderNo(orderNo);
                    orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                    continue;
                }
                PockerOrderEntity pockerOrderEntityUpStatus = new PockerOrderEntity();
                pockerOrderEntityUpStatus.setOrderStatus(1);
                pockerOrderEntityUpStatus.setOrderNo(orderNo);
                orderContract.updatePockerOrderEntity(pockerOrderEntityUpStatus);
                continue;
            } catch (Exception e) {
                logger.error("订单号: {} ,查询错误", pockerOrderEntity.getOrderNo());
            }
        }
    }

    @Override
    public ResponseData reSendCapitalAfterRisk(List<OrderInfo> orderInfos) throws Exception {
        if (orderInfos == null || orderInfos.size() < 1) {
            return ResponseData.error("请求参数异常！");
        }
        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfo.getOrderNo() == null) {
                logger.error("推送订单单号为空！");
                continue;
            }
            if (!checkOrder(orderInfo)) {
                continue;
            }
            ResponseData<OrderInfo> order = orderContract.getOrderByOrderNo(orderInfo.getOrderNo());
            orderInfo = order.getData();
            resendOrder(orderInfo);
        }
        return ResponseData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData reSendCapitalAfterRiskByOrderNo(String orderNo) throws Exception {
        if (orderNo == null || orderNo == "") {
            return ResponseData.error("请求参数异常！");
        }
        if (!checkOrder(orderNo)) {
            logger.info("该订单不允许重新发起借款：" + orderNo);
            return ResponseData.error("订单校验失败");
        }
        ResponseData<OrderInfo> order = orderContract.getOrderByOrderNo(orderNo);
        OrderInfo orderInfo = order.getData();
        //重新分配订单渠道
        String channel = null;
        ResponseData contractChannel = orderChannelContract.getChannel();
        if ("0".equals(contractChannel.getStatus())) {
            channel = (String) contractChannel.getData();
        } else {
            logger.info("没有可用渠道：" + orderNo);
            return ResponseData.error("没有可用渠道：" + orderNo);
        }
        orderInfo.setFundCode(channel);
        resendOrder(orderInfo);
        return ResponseData.success();
    }

    private void resendOrder(OrderInfo orderInfo) throws Exception {
        OrderMessage oMessage = new OrderMessage();
        oMessage.setChannel(orderInfo.getChannel());
        oMessage.setFundCode(orderInfo.getFundCode());
        oMessage.setUserId(orderInfo.getUserId());
        oMessage.setOrderNo(orderInfo.getOrderNo());
        oMessage.setIfTask(1);
        redisTemplate.delete(Constants.CAPITAL_KEY + orderInfo.getUserId());
        redisTemplate.delete(Constants.DLD_CALLBACK_PREFIX + orderInfo.getOrderNo());
        redisTemplate.delete(Constants.KDLC_CALLBACK_PREFIX + orderInfo.getOrderNo());
        if (redisTemplate.hasKey(Constants.RESEND_CAPITAL_KEY + orderInfo.getOrderNo())) {
            logger.error("重复发起的异常订单请求：" + JSON.toJSONString(orderInfo.getOrderNo()));
            throw new Exception("重复发起的异常订单请求");
        } else {
            redisTemplate.opsForValue().set(Constants.RESEND_CAPITAL_KEY + orderInfo.getOrderNo(), "1",24*60,TimeUnit.MINUTES);
        }
        reSendCapitalAfterRiskWithCheck(oMessage);
    }

    private boolean checkOrder(OrderInfo orderInfo) {
        boolean check = false;
        ResponseData<OrderInfo> order = orderContract.getOrderByOrderNo(orderInfo.getOrderNo());
        orderInfo = order.getData();
        if (orderInfo == null) {
            logger.info("未查询到订单信息！");
            return check;
        }
        ResponseData<List<OrderInfo>> orderInfos = orderContract.getLastOrderByUserId(orderInfo.getUserId());
        if (orderInfos != null) {
            List<OrderInfo> list = orderInfos.getData();
            if (list != null && orderInfos.getData().size() > 0) {
                if (orderInfo.getOrderNo().equals(list.get(0).getOrderNo())) {
                    check = true;
                }
            } else {
                check = true;
            }
        }
        return check;
    }

    private boolean checkOrder(String orderNo) {
        boolean check = false;
        ResponseData<OrderInfo> order = orderContract.getOrderByOrderNo(orderNo);
        OrderInfo orderInfo = order.getData();
        if (orderInfo == null) {
            logger.info("未查询到订单信息！");
            return check;
        }
        ResponseData<List<OrderInfo>> orderInfos = orderContract.getLastOrderByUserId(orderInfo.getUserId());
        if (orderInfos != null) {
            List<OrderInfo> list = orderInfos.getData();
            if (list != null && orderInfos.getData().size() > 0) {
                if (orderInfo.getOrderNo().equals(list.get(0).getOrderNo())) {
                    check = true;
                }
            } else {
                check = true;
            }
        }
        return check;
    }
}
