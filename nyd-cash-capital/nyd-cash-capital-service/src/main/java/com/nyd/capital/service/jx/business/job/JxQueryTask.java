package com.nyd.capital.service.jx.business.job;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.api.service.JxApi;
import com.nyd.capital.api.service.JxQueryTaskApi;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.jx.*;
import com.nyd.capital.service.UserJxService;
import com.nyd.capital.service.jx.JxService;
import com.nyd.capital.service.jx.config.JxConfig;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.DingdingUtil;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author liuqiu
 */
@Service("jxQueryTask")
public class JxQueryTask implements JxQueryTaskApi {
    private static final Logger logger = LoggerFactory.getLogger(JxQueryTask.class);
    @Autowired
    private UserJxService userJxService;
    @Autowired
    private JxService jxService;
    @Autowired
    private JxConfig jxConfig;
    @Autowired
    private JxApi jxApi;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    @Autowired
    private CrawlerUtil crawlerUtil;

//    /**
//     * 每分钟跑一次
//     */
//    @Scheduled(cron = "0 0/1 * * * ?")
//    public void test() {
//        ResponseData<WebDriver> responseData = crawlerUtil.getUrl("https://www.baidu.com", false);
//        logger.info("获取无头浏览器:" + ToStringBuilder.reflectionToString(responseData));
//        WebDriver driver = responseData.getData();
//        logger.info("源码:" + driver.getPageSource());
//    }
    /**
     * 每半小时跑一次
     */
    //@Scheduled(cron = "0 0/30 * * * ?")
    @Override
    public void queryStatusFromJx() {
        logger.info("开始跑批查询用户放款情况");
        //查询userJx表中stage为2的用户,查询放款结果,结果为成功的为其进行提现并更改stage为3
        List<UserJx> list = userJxService.selectUserStageTwo();
        logger.info("当前待放款查询的数量为：" + list.size());
        if (list.size() == 0) {
            return;
        }
        JxLoanQueryRequest jxLoanQueryRequest = new JxLoanQueryRequest();
        for (UserJx userJx : list) {
            logger.info("跑批查询该用户的即信放款情况，userJx:" + JSON.toJSONString(userJx));
            jxLoanQueryRequest.setLoanId(String.valueOf(userJx.getLoanId()));
            ResponseData responseData = jxService.jxLoanQuery(jxLoanQueryRequest);
            if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())) {
                continue;
            }
            Map<String, Object> map = (Map<String, Object>) responseData.getData();
            if ((Integer) map.get("status") == 2) {
                logger.info("该用户即信已放款，准备获取提现页面：" + map.get("loanId"));
                //已放款,进行提现
                JxWithDrawRequest jxWithDrawRequest = new JxWithDrawRequest();
                jxWithDrawRequest.setMemberId(userJx.getMemberId());
                BigDecimal amount = userJx.getAmount();
                BigDecimal scale = amount.setScale(2, BigDecimal.ROUND_UP);
                jxWithDrawRequest.setAmount(scale.toPlainString());
                jxWithDrawRequest.setLoanId(String.valueOf(userJx.getLoanId()));
                ResponseData data = jxService.jxWithDraw(jxWithDrawRequest);
                if (OpenPageConstant.STATUS_ONE.equals(data.getStatus())) {

                    continue;
                }
                ArrayList<Map<String,Object>> investments = (ArrayList) map.get("investments");

                JxWithDrawResponse jxWithDrawResponse = (JxWithDrawResponse) data.getData();
                String url = jxWithDrawResponse.getUrl();
                //处理提现
                logger.info("userJx:" + JSON.toJSONString(userJx) + "页面地址为:" + url);
                doJxWithDraw(url, userJx, investments);
            } else if ((Integer) map.get("status") == 3) {
                //如果流标的处理

                //设置即信用户提现失败
                userJx.setStage(6);
                jxApi.updateUserJx(userJx);

                OrderInfo orderInfo = capitalOrderRelationContract.selectOrderInfo(String.valueOf(userJx.getLoanId())).getData();

                RemitInfo remitInfo = new RemitInfo();

                remitInfo.setRemitTime(new Date());
                remitInfo.setOrderNo(orderInfo.getOrderNo());
                remitInfo.setRemitStatus("1");
                remitInfo.setFundCode("jx");
                remitInfo.setRemitNo(String.valueOf(userJx.getLoanId()));
                remitInfo.setChannel(orderInfo.getChannel());
                remitInfo.setRemitAmount(orderInfo.getLoanAmount());
                logger.info("放款流水:" + JSON.toJSON(remitInfo));
                try {
                    rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                    logger.info("即信放款流水发送mq成功");
                } catch (Exception e) {
                    logger.error("发送mq消息发生异常");
                }
            } else {
                continue;
            }
        }
    }

    @Override
    public void selectOrderInfosFromJx() {
        logger.info("开始跑批查询用户放款情况");
        try {
            ResponseData responseData = capitalOrderRelationContract.selectOrderInfosFromJx();
            if (OpenPageConstant.STATUS_ONE.equals(responseData.getStatus())){
                DingdingUtil.getErrMsg("跑批时出错了，注意手动处理下！");
                return;
            }
            List<OrderInfo> orderInfos = (List<OrderInfo>)responseData.getData();
            int count = 0;
            List<String> list = new ArrayList<>();
            if (orderInfos.size()>0){
                //遍历
                for(OrderInfo orderInfo : orderInfos){
                    JxQueryLoanPhasesRequest jxQueryLoanPhasesRequest = new JxQueryLoanPhasesRequest();
                    jxQueryLoanPhasesRequest.setLoanId(orderInfo.getAssetNo());
                    jxQueryLoanPhasesRequest.setStatus(0);
                    ResponseData queryLoanPhases = jxService.queryLoanPhases(jxQueryLoanPhasesRequest);
                    if (OpenPageConstant.STATUS_ONE.equals(queryLoanPhases.getStatus())){
                        count ++;
                        list.add(orderInfo.getAssetNo());
                        continue;
                    }
                    Map<String,Object> map =(Map<String,Object>) queryLoanPhases.getData();
                    ArrayList<Map<String,Object>> items = (ArrayList<Map<String,Object>>)map.get("items");
                    Map<String, Object> map1 = items.get(0);
                    Integer id = (Integer) map1.get("id");
                    Double amount = (Double) map1.get("amount");
                    JxRepaymentsRequest jxRepaymentsRequest = new JxRepaymentsRequest();
                    jxRepaymentsRequest.setLoanPhaseId(new Long(id));
                    jxRepaymentsRequest.setRequestAmount(new BigDecimal(amount).setScale(2,BigDecimal.ROUND_HALF_UP));
                    jxRepaymentsRequest.setType(2);
                    ResponseData repayments = jxService.repayments(jxRepaymentsRequest);
                    if (OpenPageConstant.STATUS_ONE.equals(repayments.getStatus())){
                        count ++;
                        list.add(orderInfo.getAssetNo());
                        continue;
                    }
                    JxRepaymentsResponse data =(JxRepaymentsResponse) repayments.getData();
                    if (!"0".equals(data.getStatusCode())){
                        count ++;
                        list.add(orderInfo.getAssetNo());
                        continue;
                    }
                    String url = data.getUrl();
                    doJxRepay(url,orderInfo.getAssetNo());
                }
                DingdingUtil.getErrMsg("跑批处理还款，总计处理："+orderInfos.size()+"条数据，其中"+count+"条数据处理时发生异常,标号为："+JSON.toJSONString(list));
            }
        }catch (Exception e){
            logger.error("跑批处理用户放款情况发生异常",e);
        }
    }

    private void doJxWithDraw(String url, UserJx userJx, ArrayList<Map<String,Object>> investments) {
        //开启一个线程去处理提现问题
        new Thread(() -> {
            try {
                logger.info("创建无头浏览器进行提现处理,url:" + url);
                ResponseData<WebDriver> data = crawlerUtil.getUrl(url);
                if (!OpenPageConstant.STATUS_ZERO.equals(data.getStatus())){
                    return;
                }
                WebDriver dr = data.getData();
                dr.get(url);
                Thread.sleep(2000);
                //进行提现
                WebElement lock = dr.findElement(By.id("lock"));
                lock.click();
                Thread.sleep(5000);
                //输入密码
                logger.info("提现页面源码为:" + dr.getPageSource());
                WebElement encPin = null;
                try {
                    encPin = dr.findElement(By.id("pass"));
                } catch (Exception e) {
                    logger.info("did not find pass try another");
                    encPin = dr.findElement(By.id("encPin"));
                }
                //获取交易密码
                String password = userJx.getPassword();
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] buffer = decoder.decodeBuffer(password);
                if (password == null) {
                    return;
                }
                encPin.sendKeys(new String(buffer));
                //点击确认
                WebElement sub = dr.findElement(By.id("sub"));
                sub.click();
                Thread.sleep(6000);
                dr.close();
                userJx.setStage(4);
                //提现后处理
                RemitMessage remitMessage = new RemitMessage();
                remitMessage.setRemitStatus("0");
                OrderInfo orderInfo = null;
                try {
                    orderInfo = capitalOrderRelationContract.selectOrderInfo(String.valueOf(userJx.getLoanId())).getData();
                    if (orderInfo == null) {
                        logger.info("根据资产号查询订单不存在");
                        return;
                    }
                } catch (Exception e) {
                    logger.error("根据资产号查询订单发生异常");
                    return;
                }
                logger.info("orderInfo:" + JSON.toJSONString(orderInfo));
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
                if (investments.size() > 0) {
                    for (Map<String,Object> investment : investments) {

                        RemitInfo remitInfo = new RemitInfo();

                        remitInfo.setRemitTime(new Date());
                        remitInfo.setOrderNo(orderInfo.getOrderNo());
                        remitInfo.setRemitStatus("0");
                        remitInfo.setFundCode("jx");
                        remitInfo.setRemitNo(String.valueOf(userJx.getLoanId()));
                        remitInfo.setChannel(orderInfo.getChannel());
                        remitInfo.setRemitAmount(new BigDecimal((Integer) investment.get("amount")));
                        remitInfo.setInvestorId((String) investment.get("investorIdCardNumber"));
                        remitInfo.setInvestorName((String) investment.get("investorName"));
                        logger.info("放款流水:" + JSON.toJSON(remitInfo));
                        try {
                            rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                            logger.info("即信放款流水发送mq成功");
                        } catch (Exception e) {
                            logger.error("发送mq消息发生异常");
                        }
                    }
                }

            } catch (Throwable e) {
                logger.error("创建无头浏览器进行提现处理发生异常,url:" + url, e);
            }
            jxApi.updateUserJx(userJx);
        }).start();
    }

    private void doJxRepay(String url,String loanId) {
        //开启一个线程去处理还款问题
        new Thread(() -> {
            try {
                logger.info("创建无头浏览器进行还款处理,url:" + url);
                ResponseData<WebDriver> data = crawlerUtil.getUrl(url);
                if (!OpenPageConstant.STATUS_ZERO.equals(data.getStatus())){
                    return;
                }
                WebDriver dr = data.getData();
                Thread.sleep(2000);
                logger.info("还款页面源码为:" + dr.getPageSource());
                //获取确认还款按钮
                WebElement repay = dr.findElement(By.id("repay"));
                //点击
                Thread.sleep(1000);
                repay.click();
                Thread.sleep(5000);
                dr.close();

            } catch (Throwable e) {
                logger.error("创建无头浏览器进行还款处理发生异常,url:" + url+"标号为："+loanId, e);
                DingdingUtil.getErrMsg("爬虫处理还款时发生异常，url:"+url+"标号为："+loanId);
            }
        }).start();
    }
}
