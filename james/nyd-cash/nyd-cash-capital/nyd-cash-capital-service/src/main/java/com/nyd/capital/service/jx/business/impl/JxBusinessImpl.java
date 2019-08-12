package com.nyd.capital.service.jx.business.impl;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.api.service.JxApi;
import com.nyd.capital.entity.UserJx;
import com.nyd.capital.model.RemitMessage;
import com.nyd.capital.model.jx.*;
import com.nyd.capital.service.UserJxService;
import com.nyd.capital.service.jx.JxService;
import com.nyd.capital.service.jx.business.JxBusiness;
import com.nyd.capital.service.jx.business.run.SubmitRunnable;
import com.nyd.capital.service.jx.config.JxConfig;
import com.nyd.capital.service.pocket.run.MapBean;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.capital.service.utils.Constants;
import com.nyd.order.api.CapitalOrderRelationContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Service
public class JxBusinessImpl implements JxBusiness {

    Logger logger = LoggerFactory.getLogger(JxBusinessImpl.class);

    @Autowired
    private JxService jxService;
    @Autowired
    private JxConfig jxConfig;
    @Autowired
    private MapBean mapBean;
    @Autowired
    private UserJxService userJxService;
    @Autowired
    private JxApi jxApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private CapitalOrderRelationContract capitalOrderRelationContract;
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    @Autowired
    private CrawlerUtil crawlerUtil;

    @Override
    public ResponseData openJxHtml(OpenJxHtmlRequest request) {
        if (request == null || StringUtils.isBlank(request.getUserId()) || StringUtils.isBlank(request.getStage())) {
            return ResponseData.error("参数错误");
        }
        ResponseData response = ResponseData.success();
        try {
            logger.info("进入打开中网国投的页面的请求;" + JSON.toJSONString(request));
            String url = null;
            //定义一个变量flag用于判断爬虫的走向
            boolean flag = false;
            ResponseData informationByUserId = null;
            if (StringUtils.isBlank(request.getMobile()) && StringUtils.isBlank(request.getIdCardNumber())) {
                informationByUserId = jxApi.getInformationByUserId(request.getUserId());
                JxUserDetail userDetail = (JxUserDetail) informationByUserId.getData();
                request.setIdCardNumber(userDetail.getIdNumber());
                if (StringUtils.isBlank(request.getBankCardNumber())) {
                    request.setBankCardNumber(userDetail.getBankAccount());
                }
                request.setRealName(userDetail.getRealName());
                request.setMobile(userDetail.getAccountNumber());
            }
            JxFiveComprehensiveRequest jxFiveComprehensiveRequest = new JxFiveComprehensiveRequest();
            jxFiveComprehensiveRequest.setMobile(request.getMobile());
            jxFiveComprehensiveRequest.setRealName(request.getRealName());
            jxFiveComprehensiveRequest.setIdCardNumber(request.getIdCardNumber());
            jxFiveComprehensiveRequest.setBankCardNumber(request.getBankCardNumber());
            //回调地址
            jxFiveComprehensiveRequest.setReturnUrl(jxConfig.getCallBack() + "?jxCode=" + request.getUserId());
            ResponseData responseData = jxService.jxFiveComprehensive(jxFiveComprehensiveRequest);
            JxFiveComprehensiveResponse data = (JxFiveComprehensiveResponse) responseData.getData();
            //获取页面
            url = data.getUrl();
            flag = true;
            //获取进度
            String stage = request.getStage();
            if (OpenPageConstant.OPEN_PAGE_STAGE_IDCARD.equals(stage)) {
                if (redisTemplate.hasKey(OpenPageConstant.OPEM_PAGE_FOR + request.getUserId())) {
                    logger.info("有值" + request.getUserId());
                    ResponseData r = ResponseData.error("不能重复提交");
                    r.setData(null);
                    return r;
                } else {
                    redisTemplate.opsForValue().set(OpenPageConstant.OPEM_PAGE_FOR + request.getUserId(), "1", 120, TimeUnit.SECONDS);
                }
                //进行江西银行开户
                Map<String, Object> map = new HashMap<>();
                UUID uuid = UUID.randomUUID();
                map.put("driveruuid", uuid);
                map.put("userId", request.getUserId());
                openPage(url, request, uuid);
                return response.setData(map);
            }
            SubmitRunnable runnable = new SubmitRunnable(flag, request, url, stage, userJxService, jxConfig, redisTemplate,crawlerUtil);
            logger.info("进入自动开户页面线程:" + JSON.toJSONString(request));
            threadPoolTaskExecutor.execute(runnable);
        } catch (Exception e) {
            logger.error("打开中网国投的页面的请求进行中发生异常", e);
        }
        return ResponseData.success();
    }


    @Override
    public ResponseData submitJxMsgCode(SubmitJxMsgCode request) {
        ResponseData responseData = ResponseData.success();
        try {
            logger.info("进入获取江西银行开户短信验证码的请求;" + JSON.toJSONString(request));
            logger.info("begin to submitOpenHtml,and request is " + request);
            if (request == null || StringUtils.isBlank(request.getSmsCode()) || StringUtils.isBlank(request.getDriverUuid())) {
                return ResponseData.error("参数错误");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("code", request.getSmsCode());
            map.put("time", new Date());
            map.put("userId", request.getUserId());
            redisTemplate.opsForValue().set(OpenPageConstant.OPEM_PAGE_REDIS_CODE + request.getDriverUuid(), JSON.toJSONString(map), 100, TimeUnit.MINUTES);
            return responseData;
        } catch (Exception e) {
            logger.error("获取江西银行开户短信验证码的请求进行中发生异常", e);
            return ResponseData.error("设置收款银行卡不成功!");
        }
    }

    @Override
    public ResponseData jxReturnForHtml(String jxCode) {
        ResponseData response = ResponseData.success();
        try {
            logger.info("进入五合一开户回调,and request is " + jxCode);
            if (StringUtils.isBlank(jxCode)) {
                return ResponseData.error(OpenPageConstant.PRARM_ERROR);
            }
            // 避免重复的通知
            try {
                if (redisTemplate.hasKey(Constants.JX_CALLBACK_HTML + jxCode)) {
                    logger.error("有重复通知" + JSON.toJSONString(jxCode));
                    return ResponseData.success("通知重复");
                } else {
                    redisTemplate.opsForValue().set(Constants.JX_CALLBACK_HTML + jxCode, "1", 2880, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                logger.error("写redis出错" + e.getMessage());
            }
            ResponseData informationByUserId = jxApi.getInformationByUserId(jxCode);
            if (OpenPageConstant.STATUS_ONE.equals(informationByUserId.getStatus())) {
                logger.error("获取用户相关信息失败");
                return ResponseData.error(OpenPageConstant.DUBBO_ERROR);
            }
            JxUserDetail userDetail = (JxUserDetail) informationByUserId.getData();
            JxQueryPushStatusRequest jxQueryPushStatusRequest = new JxQueryPushStatusRequest();
            jxQueryPushStatusRequest.setBankCardNumber(userDetail.getBankAccount());
            jxQueryPushStatusRequest.setIdCardNumber(userDetail.getIdNumber());
            jxQueryPushStatusRequest.setMobile(userDetail.getAccountNumber());
            jxQueryPushStatusRequest.setRealName(userDetail.getRealName());
            ResponseData responseData = jxService.queryPushStatus(jxQueryPushStatusRequest);
            JxQueryPushStatusResponse jxQueryPushStatusResponse = (JxQueryPushStatusResponse) responseData.getData();
            userJxService.updateMember(jxQueryPushStatusResponse.getMemberId(), jxCode);
        } catch (Exception e) {
            logger.error("即信返回发生异常", e);
            return ResponseData.error("即信返回发生异常");
        }
        logger.info("五合一开户回调成功,and response is" + JSON.toJSONString(response));
        return response;
    }


    @Override
    public ResponseData jxReturnForWithdraw(ReturnForWithdrawRequest returnForWithdrawRequest) {
        // 避免重复的通知
        try {
            if (redisTemplate.hasKey(returnForWithdrawRequest.getLoanId() + Constants.JX_CALLBACK_WITHDRAW + returnForWithdrawRequest.getMemberId())) {
                logger.error("有重复通知" + JSON.toJSONString(returnForWithdrawRequest));
                return ResponseData.success("通知重复");
            } else {
                redisTemplate.opsForValue().set(returnForWithdrawRequest.getLoanId() + Constants.JX_CALLBACK_WITHDRAW + returnForWithdrawRequest.getMemberId(), "1", 2880, TimeUnit.MINUTES);
            }

        } catch (Exception e) {
            logger.error("提现回调发生异常", e);
        }
        return ResponseData.success();
    }

    @Override
    public ResponseData onlyForWithdraw(ReturnForWithdrawRequest returnForWithdrawRequest) {
        // 避免重复的通知
        try {
            if (StringUtils.isBlank(returnForWithdrawRequest.getLoanId())) {
                return ResponseData.error("标ID不能为空！");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = formatter.parse("2018-09-03 19:00:21");
            RemitMessage remitMessage = new RemitMessage();
            remitMessage.setRemitStatus("0");
            OrderInfo orderInfo = null;
            try {
                orderInfo = capitalOrderRelationContract.selectOrderInfo(String.valueOf(returnForWithdrawRequest.getLoanId())).getData();
                if (orderInfo == null) {
                    logger.info("根据资产号查询订单不存在");
                    return ResponseData.error("根据资产号查询订单不存在");
                }
            } catch (Exception e) {
                logger.error("根据资产号查询订单发生异常");
                return ResponseData.error("根据资产号查询订单发生异常");
            }
            logger.info("orderInfo:" + JSON.toJSONString(orderInfo));
            remitMessage.setRemitTime(d1);
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
            remitInfo.setRemitTime(d1);
            remitInfo.setOrderNo(orderInfo.getOrderNo());
            remitInfo.setRemitStatus("0");
            remitInfo.setFundCode("jx");
            remitInfo.setRemitNo(String.valueOf(returnForWithdrawRequest.getLoanId()));
            remitInfo.setChannel(orderInfo.getChannel());
            remitInfo.setRemitAmount(orderInfo.getLoanAmount());
            logger.info("放款流水:" + JSON.toJSON(remitInfo));
            try {
                rabbitmqProducerProxy.convertAndSend("remitLog.nyd", remitInfo);
                logger.info("即信放款流水发送mq成功");
            } catch (Exception e) {
                logger.error("发送mq消息发生异常");
            }
        } catch (Exception e) {
            logger.error("修复数据发生异常", e);
        }
        return ResponseData.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData selectJxAccount(OrderMessage message) throws Exception {
        ResponseData response = ResponseData.success();
        String stage = null;
        Map<String, Object> map = new HashMap<>();
        JxUserDetail userInfo = null;
        try {
            logger.info("查询用户在即信的开户情况:" + message.toString());
            if (redisTemplate.hasKey("kzjr2qcgz" + message.getUserId())) {
                logger.info("有值,已经开户失败转单" + message.getUserId());
                message.setFundCode("qcgz");
            }
            //查询数据库
            ResponseData userJxByUserId = jxApi.getUserJxByUserId(message.getUserId());
            List<UserJx> userJxs = (List<UserJx>) userJxByUserId.getData();
            if (userJxs.size() > 0 && userJxs.get(0).getStage() == 4) {
                stage = "6";
            } else {
                ResponseData informationByUserId = jxApi.getInformationByUserId(message.getUserId());
                if (OpenPageConstant.STATUS_ONE.equals(informationByUserId.getStatus())) {
                    return ResponseData.error("查询用户信息有误");
                }
                userInfo = (JxUserDetail) informationByUserId.getData();
                userInfo.setBankAccount(message.getBankAccount());
                JxQueryPushStatusRequest jxQueryPushStatusRequest = new JxQueryPushStatusRequest();
                jxQueryPushStatusRequest.setBankCardNumber(message.getBankAccount());
                jxQueryPushStatusRequest.setIdCardNumber(userInfo.getIdNumber());
                jxQueryPushStatusRequest.setMobile(userInfo.getAccountNumber());
                jxQueryPushStatusRequest.setRealName(userInfo.getRealName());
                ResponseData responseData = null;
                try {
                    responseData = jxApi.queryPushStatus(jxQueryPushStatusRequest);
                    logger.info("查询用户在即信开户情况结果: " + JSON.toJSONString(responseData));
                } catch (Exception e) {
                    logger.info("调用dubbo查询该用户在即信开户情况失败,分配到七彩格子,userInfo:" + userInfo.toString());
                    message.setFundCode("qcgz");
                }
                if ("0".equals(responseData.getStatus())) {
                    JxQueryPushStatusResponse jxQueryPushStatusResponse = (JxQueryPushStatusResponse) responseData.getData();
                    if (!jxQueryPushStatusResponse.isAllowPass()) {
                        message.setFundCode("qcgz");
                        logger.info("中网国投关闭了通道，统一将用户分到七彩格子，,userInfo:" + userInfo.toString());
                    } else if (jxQueryPushStatusResponse.isAllowPass() && StringUtils.isNotBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isHasOpenAccount() && jxQueryPushStatusResponse.isBankCardHasBound() && !jxQueryPushStatusResponse.isBindingCardDifferent() && jxQueryPushStatusResponse.isTradePasswordHasSet() && jxQueryPushStatusResponse.isPaymentDelegationHasSigned() && jxQueryPushStatusResponse.isRepaymentDelegationHasSigned() && jxQueryPushStatusResponse.isAccreditFdd()) {
                        //信息全部填写,则跳过爬虫流程,直接去推单外审(查询用户是否去到开通存管页面)
                        if (userJxs.size() == 0) {
                            message.setFundCode("qcgz");
                        } else {
                            UserJx userJx = userJxs.get(0);
                            if (userJx.getMemberId() == null) {
                                userJx.setMemberId(jxQueryPushStatusResponse.getMemberId());
                                userJx.setStage(1);
                                jxApi.updateUserJx(userJx);
                            }
                            stage = "6";
                            logger.info("该用户已在即信开户,跳过开户流程:" + map + ",userInfo:" + userInfo.toString());
                        }
                    } else if ((StringUtils.isNotBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isBindingCardDifferent()) || (StringUtils.isBlank(jxQueryPushStatusResponse.getMemberId()) && jxQueryPushStatusResponse.isIdCardIsUsed())) {
                        message.setFundCode("qcgz");
                        logger.info("该用户绑定信息不一致,暂时不处理,分配到七彩格子,userInfo:" + userInfo.toString());
                    } else {
                        //判定用户进行到了哪一步
                        if (StringUtils.isBlank(jxQueryPushStatusResponse.getMemberId())) {
                            stage = OpenPageConstant.OPEN_PAGE_STAGE_IDCARD;
                        } else if (!jxQueryPushStatusResponse.isTradePasswordHasSet()) {
                            stage = OpenPageConstant.OPEN_PAGE_STAGE_TRADE;
                        } else if (!jxQueryPushStatusResponse.isPaymentDelegationHasSigned()) {
                            stage = OpenPageConstant.OPEN_PAGE_STAGE_PAYMENT;
                        } else if (!jxQueryPushStatusResponse.isRepaymentDelegationHasSigned()) {
                            stage = OpenPageConstant.OPEN_PAGE_STAGE_REPAYMENT;
                        } else if (!jxQueryPushStatusResponse.isAccreditFdd()) {
                            stage = OpenPageConstant.OPEN_PAGE_STAGE_ACCREDIT;
                        }
                        logger.info("该用户需要去即信开户或完善开户:" + ",userInfo:" + userInfo.toString());
                    }

                } else {
                    message.setFundCode("qcgz");
                    logger.info("查询该用户在即信开户情况失败,分配到七彩格子,userInfo:" + userInfo.toString());
                }
            }
        } catch (Exception e) {
            message.setFundCode("qcgz");
            logger.info("查询该用户在即信开户情况发生异常,分配到七彩格子,userInfo:" + message.toString(), e);
        }

        if ("qcgz".equals(message.getFundCode())) {
            //根据订单号修改order
            ResponseData responseData2 = null;
            try {
                responseData2 = capitalOrderRelationContract.updateFundCode(message.getOrderNo());
            } catch (Exception e) {
                throw e;
            }
            if ("1".equals(responseData2.getStatus())) {
                logger.error("修改订单渠道时发生异常");
                return ResponseData.error("修改订单渠道时发生异常");
            }
            stage = "7";
        }
        map.put("stage", stage);
        map.put("userInfo", userInfo);
        map.put("message", message);
        response.setData(map);
        return response;
    }


    /**
     * 实名认证
     *
     * @param url
     * @param request
     * @param uuid
     */
    private void openPage(String url, OpenJxHtmlRequest request, UUID uuid) {
        new Thread(() -> {
            try {
                ResponseData responseData = crawlerUtil.getUrl(url);
                if (OpenPageConstant.STATUS_ONE.equals(responseData)) {
                    return;
                }
                WebDriver dr = (WebDriver) responseData.getData();
                try {
                    Thread.sleep(2000);
                    //logger.info("打开页面的源码是:" + dr.getPageSource());
                    //获取实名认证DOM
                    WebElement realName_state = dr.findElement(By.id("realName_state"));
                    //点击实名认证
                    realName_state.click();
                    Thread.sleep(3000);
                    //logger.info("实名认证的页面的源码是:" + dr.getPageSource());
                    //进入到实名认证页面
                    //获取姓名DOM
                    WebElement realName = dr.findElement(By.id("realName"));
                    //设置姓名
                    realName.sendKeys(request.getRealName());
                    //获取身份证号DOM
                    WebElement certNo = dr.findElement(By.id("certNo"));
                    //设置身份证号
                    certNo.sendKeys(request.getIdCardNumber());
                    //获取预留手机号
                    WebElement mobile = dr.findElement(By.id("mobile"));
                    //设置预留手机号
                    mobile.sendKeys(request.getMobile());
                    //获取我同意的单选框
                    WebElement checkbox = dr.findElement(By.id("checkbox"));
                    //点击勾选
                    checkbox.sendKeys(Keys.SPACE);
                    //获取实名认证确认按钮
                    WebElement authenticationSubmit = dr.findElement(By.id("authenticationSubmit"));
                    //点击提交实名认证信息
                    authenticationSubmit.click();
                    Thread.sleep(4000);
                    //进入江西银行开户页面
                    //获取绑定银行卡号DOM
                    logger.info("江西银行开户页面的源码是:" + dr.getPageSource());
                    WebElement cardNo = dr.findElement(By.id("BIND_CARD_NO"));
                    //将用户默认银行卡号设置进去
                    cardNo.sendKeys(request.getBankCardNumber());

                    redisTemplate.opsForValue().set(OpenPageConstant.OPEM_PAGE_BANK + request.getUserId(), JSON.toJSON(request.getBankCardNumber()), 120, TimeUnit.SECONDS);
                    //针对获取验证码的id随机变动而做的选择判断(此处有坑,江西银行开户页面似乎DOM中id随机变动采取的反爬取措施)
                    WebElement getSms = null;
                    try {
                        getSms = dr.findElement(By.id("appGetSmsCode"));
                    } catch (Throwable e) {
                        logger.error("did not find appGetSmsCode try another", e);
                        getSms = dr.findElement(By.id("smsBtn"));
                    }
                    //得到获取验证码DOM后进行点击动作
                    getSms.click();
                    mapBean.getConcurrentHashMap().putIfAbsent(uuid.toString(), dr);
                    logger.info(request.getUserId() + "点击结果成功");
                } catch (Throwable e) {
                    logger.info(url + "爬虫处理时出现异常", e);
                }
            } catch (Throwable e) {
                logger.error("创建无头浏览器进行提现处理发生异常,url:" + url, e);
            }
        }).start();

    }

}
