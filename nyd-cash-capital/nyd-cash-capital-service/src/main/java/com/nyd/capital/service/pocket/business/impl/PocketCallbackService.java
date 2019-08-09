package com.nyd.capital.service.pocket.business.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.PocketAccountEnum;
import com.nyd.capital.model.enums.PocketTxCodeEnum;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.pocket.run.MsgCodeRunnable;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.nyd.capital.service.utils.DateUtil;
import com.nyd.capital.service.utils.RedisUtil;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author liuqiu
 */
@Service
public class PocketCallbackService {

    private static Logger logger = LoggerFactory.getLogger(PocketCallbackService.class);

    @Autowired
    private OrderContract orderContract;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    @Autowired
    private Pocket2Service pocket2Service;
    @Autowired
    private PocketConfig pocketConfig;
    @Autowired
    private CrawlerUtil crawlerUtil;
    @Autowired
    private UserPocketService userPocketService;
    @Autowired
    private RabbitmqProducerProxy proxy;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    public ResponseData callback(PocketCallbackMessage message) {
        logger.info("begin pocket callback,and message is:" + message.toString());
        try {
            String txCode = message.getTxCode();
            String orderId = message.getOrderId();
            ResponseData<OrderInfo> info = capitalOrderRelationContract.selectOrderInfo(orderId);
            if (!OpenPageConstant.STATUS_ZERO.equals(info.getStatus())) {
                return ResponseData.error("select pocket order error");
            }
            OrderInfo data = info.getData();
            UserPocket pocket = userPocketService.selectPocketUserByUserId(data.getUserId());
            if (pocket == null) {
                return ResponseData.error();
            }
            if (PocketTxCodeEnum.createOrderLendPay.getCode().equals(txCode)) {
//                if (redisTemplate.hasKey(OpenPageConstant.NEW_POCKET_CALLBACK_LOAN + data.getOrderNo())) {
//                    logger.error("repeat callback");
//                    return ResponseData.error("repeat callback");
//                } else {
//                    redisTemplate.opsForValue().set(OpenPageConstant.NEW_POCKET_CALLBACK_LOAN  + data.getOrderNo(), "1");
//                }
                if (!Integer.valueOf(PocketAccountEnum.Have_Accepted.getCode()).equals(pocket.getStage())) {
                    return ResponseData.error();
                }
                //放款回调,进行提现处理
                if (message.isFail() || Integer.valueOf(message.getStatus()) != 1) {
                    //借款失败
                    DingdingUtil.getErrMsg("pocket order loan fail by --> " + message.getRetMsg() + ",and orderNo is:" + data.getOrderNo());
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setOrderNo(data.getOrderNo());
                    orderInfo.setOrderStatus(40);
                    orderContract.updateOrderInfo(orderInfo);
                    return ResponseData.success();
                }
                if (!message.isFail()) {
                    if (Integer.valueOf(message.getStatus()) == 1) {
                        //放款成功,进行提现处理
                        try {
                            boolean loan = redisUtil.lock("loan" + pocket.getUserId(), 1);
                            if (!loan) {
                                logger.info("other service is doing");
                                return ResponseData.error("other service is doing");
                            }
                            String password = userPocketService.selectPasswordByUserId(data.getUserId());
                            pocket.setStage(Integer.valueOf(PocketAccountEnum.Withdrawal_Ing.getCode()));
                            userPocketService.update(pocket);
                            withdrawal(data.getOrderNo(), password);
                            redisUtil.unlock("loan" + pocket.getUserId());
                        } catch (Exception e) {
                            redisUtil.unlock("loan" + pocket.getUserId());
                        }
                    }
                }
            }
            if (PocketTxCodeEnum.withdraw.getCode().equals(txCode)) {
//                if (redisTemplate.hasKey(OpenPageConstant.NEW_POCKET_CALLBACK_WITHDRAW + data.getOrderNo())) {
//                    logger.error("repeat callback");
//                    return ResponseData.error("repeat callback");
//                } else {
//                    redisTemplate.opsForValue().set(OpenPageConstant.NEW_POCKET_CALLBACK_WITHDRAW  + data.getOrderNo(), "1");
//                }
                //提现回调
                if (!Integer.valueOf(PocketAccountEnum.Withdrawal_Ing.getCode()).equals(pocket.getStage())) {
                    return ResponseData.error();
                }
                if (message.isFail() || Integer.valueOf(message.getStatus()) != 1) {
                    //提现失败
                    DingdingUtil.getErrMsg("pocket order withdraw fail by --> " + message.getRetMsg() + ",and orderNo is:" + data.getOrderNo());
                    return ResponseData.success();
                }
                if (!message.isFail()) {
                    if (Integer.valueOf(message.getStatus()) == 1) {
                        try {
                            boolean withdrawal = redisUtil.lock("withdrawal" + pocket.getUserId(), 1);
                            if (!withdrawal) {
                                logger.info("other service is doing");
                                return ResponseData.error("other service is doing");
                            }
                            //提现成功
                            pocket.setStage(Integer.valueOf(PocketAccountEnum.Have_Withdrawal.getCode()));
                            userPocketService.update(pocket);
                            //发送消息
                            handMq(data, pocket);
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        }catch (Exception e){
                            redisUtil.unlock("withdrawal" + pocket.getUserId());
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("pocket callback has exception,and message is:" + message.toString());
            return ResponseData.error("pocket callback has exception");
        }
        return ResponseData.success();
    }

    public void withdrawal(String orderNo, String password) {
        new Thread(() -> {
            //处理提现
            logger.info("begin withdrawal thread,and orderNo is:" +orderNo);
            WebDriver driver = null;
            try {
                PocketWithdrawDto withdrawDto = new PocketWithdrawDto();
                withdrawDto.setIsUrl("1");
                withdrawDto.setOutTradeNo(orderNo);
                withdrawDto.setRetUrl(pocketConfig.getPocketWithdrawRetUrl()+"?orderNo="+orderNo);
                ResponseData<PocketParentResult> withdraw = pocket2Service.withdraw(withdrawDto);
                if (!OpenPageConstant.STATUS_ZERO.equals(withdraw.getStatus())) {
                    logger.error(withdraw.getMsg());
                    return;
                }
                PocketParentResult result = withdraw.getData();
                if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())){
                    return;
                }
                String url = MsgCodeRunnable.getPocketResultUrl(withdraw);
                logger.info("withdrawal url is:"+url+"and orderNo is:" +orderNo);
                ResponseData<WebDriver> responseData = crawlerUtil.getUrl(url);
                if (!OpenPageConstant.STATUS_ZERO.equals(responseData.getStatus())) {
                    return;
                }
                driver = responseData.getData();

                WebElement encPin = driver.findElement(By.id("encPin"));
                //查询用户密码
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] buffer = decoder.decodeBuffer(password);
                encPin.sendKeys(new String(buffer));
                WebElement sub = driver.findElement(By.id("sub"));
                sub.click();
                Thread.sleep(6000);
                driver.close();
            } catch (Exception e) {
                if (driver != null) {
                    driver.close();
                }
                DingdingUtil.getErrMsg("pocket withdrawal fail ,please withdrawal again by person,orderNo is:" + orderNo);
                return;
            }
        }).start();
    }

    /**
     * 处理mq消息
     */
    public void handMq(OrderInfo orderInfo, UserPocket pocket) {
        //推送还款计划
        PocketPushAssetRepaymentPeriodDto dto = new PocketPushAssetRepaymentPeriodDto();
        String orderNo = orderInfo.getOrderNo();
        dto.setOutTradeNo(orderNo);
        PocketRepaymentBase pocketRepaymentBase = new PocketRepaymentBase();
        pocketRepaymentBase.setPeriod("1");
        pocketRepaymentBase.setRepaymentPrincipal((orderInfo.getLoanAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
        pocketRepaymentBase.setRepaymentTime(String.valueOf((DateUtil.addDayFromRemitTime(new Date(),orderInfo.getBorrowTime())).getTime() / 1000));
        pocketRepaymentBase.setRepaymentInterest((new BigDecimal(0.05).multiply(new BigDecimal(orderInfo.getBorrowTime()).setScale(1, BigDecimal.ROUND_DOWN)).multiply(orderInfo.getLoanAmount()).setScale(0,BigDecimal.ROUND_DOWN)).toPlainString());
//        pocketRepaymentBase.setRepaymentInterest((orderInfo.getInterest().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
//        pocketRepaymentBase.setRepaymentAmount((orderInfo.getRepayTotalAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
        pocketRepaymentBase.setRepaymentAmount(String.valueOf(Integer.valueOf(orderInfo.getLoanAmount().toPlainString()) + Integer.valueOf(pocketRepaymentBase.getRepaymentInterest())));
        pocketRepaymentBase.setRepaymentType("2");
        dto.setRepaymentBase(pocketRepaymentBase);
        PocketPeriodBase pocketPeriodBase = new PocketPeriodBase();
        pocketPeriodBase.setApr(orderInfo.getAnnualizedRate().toPlainString());
        pocketPeriodBase.setPeriod("1");
        pocketPeriodBase.setPlanRepaymentInterest( pocketRepaymentBase.getRepaymentInterest());
        pocketPeriodBase.setPlanRepaymentMoney( pocketRepaymentBase.getRepaymentAmount());
        pocketPeriodBase.setPlanRepaymentPrincipal((orderInfo.getLoanAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN)).toPlainString());
        pocketPeriodBase.setPlanRepaymentTime(String.valueOf((DateUtil.addDayFromRemitTime(new Date(),orderInfo.getBorrowTime())).getTime() / 1000));
        List<PocketPeriodBase> list = new ArrayList<>();
        list.add(pocketPeriodBase);
        dto.setPeriodBase(list);
        ResponseData<PocketParentResult> data = pocket2Service.pushAssetRepaymentPeriod(dto);
        if (!OpenPageConstant.STATUS_ZERO.equals(data.getStatus())){
            DingdingUtil.getErrMsg("push repayment period to pocket error,orderNo is :" + orderNo);
        }
        PocketParentResult result = data.getData();
        if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())){
            if (result.getRetMsg().contains("还款计划重复") || "50018".equals(result.getRetCode())){

            }else {
                DingdingUtil.getErrMsg("push repayment period to pocket error,msg is :" + result.getRetMsg() + ",orderNo is :" + orderNo);
            }
        }
        RemitMessage remitMessage = new RemitMessage();
        remitMessage.setRemitStatus("0");
        remitMessage.setRemitAmount(orderInfo.getLoanAmount());
        remitMessage.setOrderNo(orderNo);
        proxy.convertAndSend("remit.nyd", remitMessage);
        //如果是null 默认为nyd的订单来源
        if (orderInfo.getChannel() == null) {
            orderInfo.setChannel(BorrowConfirmChannel.NYD.getChannel());
        }
        //发送 到 ibank
        if (orderInfo.getChannel().equals(BorrowConfirmChannel.YMT.getChannel())) {
            remitMessage.setOrderNo(orderInfo.getIbankOrderNo());
            logger.info("pocket loan success and send mq to iBank." + JSON.toJSONString(remitMessage));
            proxy.convertAndSend("remitIbankOrder.ymt", remitMessage);
        }

        RemitInfo remitInfo = new RemitInfo();
        remitInfo.setRemitTime(new Date());
        remitInfo.setOrderNo(orderNo);
        remitInfo.setRemitStatus("0");
        remitInfo.setFundCode(FundSourceEnum.KDLC.getCode());
        remitInfo.setChannel(orderInfo.getChannel());
        remitInfo.setRemitAmount(orderInfo.getLoanAmount());
        logger.info("pocket loan remit is:" + JSON.toJSON(remitInfo));
        try {
            proxy.convertAndSend("remitLog.nyd", remitInfo);
            logger.info("pocket loan remit send mq success");
        } catch (Exception e) {
            logger.error("pocket loan remit send mq error");
            DingdingUtil.getErrMsg("口袋理财放款成功,发送mq消息生成放款记录时发生异常,需要补放款记录,入参为:" + JSON.toJSONString(remitInfo));
        }
        pocket.setStage(Integer.valueOf(PocketAccountEnum.Have_Finish.getCode()));
        userPocketService.update(pocket);
    }
}
