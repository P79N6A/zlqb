package com.nyd.capital.service.pocket.business.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.entity.UserPocket;
import com.nyd.capital.model.enums.FundSourceEnum;
import com.nyd.capital.model.enums.PocketAccountEnum;
import com.nyd.capital.model.enums.PocketTxCodeEnum;
import com.nyd.capital.model.jx.OpenJxHtmlRequest;
import com.nyd.capital.model.jx.SubmitJxMsgCode;
import com.nyd.capital.model.pocket.*;
import com.nyd.capital.service.CapitalService;
import com.nyd.capital.service.UserPocketService;
import com.nyd.capital.service.mq.PocketCallbackProducer;
import com.nyd.capital.service.pocket.job.PocketTask;
import com.nyd.capital.service.pocket.run.MsgCodeRunnable;
import com.nyd.capital.service.pocket.run.MapBean;
import com.nyd.capital.service.jx.config.OpenPageConstant;
import com.nyd.capital.service.jx.util.PasswordUtil;
import com.nyd.capital.service.pocket.business.Pocket2Business;
import com.nyd.capital.service.pocket.service.Pocket2Service;
import com.nyd.capital.service.pocket.util.CrawlerUtil;
import com.nyd.capital.service.pocket.util.KdaiSignUtils;
import com.nyd.capital.service.pocket.util.PocketConfig;
import com.nyd.capital.service.pocket.util.UnicodeUtil;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.msg.OrderMessage;
import com.nyd.user.api.UserBankContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiu
 */
@Service
public class Pocket2BusinessImpl implements Pocket2Business {

    private static Logger logger = LoggerFactory.getLogger(Pocket2BusinessImpl.class);

    @Autowired
    private Pocket2Service pocket2Service;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private PocketConfig pocketConfig;
    @Autowired
    private MapBean mapBean;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserPocketService userPocketService;
    @Autowired
    private CrawlerUtil crawlerUtil;
    @Autowired
    private PocketCallbackProducer producer;
    @Autowired
    private OrderContract orderContract;
    @Autowired
    private CapitalService capitalService;
    @Autowired
    private PocketTask pocketTask;
    @Autowired
    private UserBankContract userBankContract;

    @Override
    public ResponseData<String> getHtml(String userId,String mobile) {
        logger.info("begin get html and param is:" + userId);
        ResponseData<String> responseData = new ResponseData<>();
        try {
            ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(userId);
            if (!OpenPageConstant.STATUS_ZERO.equals(userInfo.getStatus())) {
                responseData.setMsg("select user has exception and userId is:" + userId);
                responseData.setStatus(OpenPageConstant.STATUS_ONE);
                return responseData;
            }
            UserInfo data = userInfo.getData();
            PocketAccountOpenEncryptPageDto dto = new PocketAccountOpenEncryptPageDto();
            dto.setName(UnicodeUtil.gbEncoding(data.getRealName()));
            dto.setMobile(mobile);
            dto.setIsUrl("1");
            String gender = data.getGender();
            if (OpenPageConstant.MEN.equals(gender)) {
                gender = "M";
            } else {
                gender = "F";
            }
            dto.setGender(gender);
            dto.setRetUrl(pocketConfig.getPocketOpenPageRetUrl()+"?userId="+userId);
            ResponseData<PocketParentResult> encryptPage = pocket2Service.accountOpenEncryptPage(dto);
            logger.info("open html result is:" + ToStringBuilder.reflectionToString(encryptPage));
            if (!OpenPageConstant.STATUS_ZERO.equals(encryptPage.getStatus())) {
                responseData.setMsg("pocket open page has exception,and param is:" + dto.toString());
                responseData.setStatus(OpenPageConstant.STATUS_ONE);
                return responseData;
            }
            PocketParentResult pageData = encryptPage.getData();
            if (!OpenPageConstant.STATUS_ZERO.equals(pageData.getRetCode())) {
                responseData.setMsg("pocket open page error,and param is:" + dto.toString());
                responseData.setStatus(OpenPageConstant.STATUS_ONE);
                return responseData;
            }
            String url = MsgCodeRunnable.getPocketResultUrl(encryptPage);
            responseData.setData(url);
            responseData.setStatus(OpenPageConstant.STATUS_ZERO);
            return responseData;
        } catch (Exception e) {
            logger.error("get html has exception ,and param is:" + userId, e);
            responseData.setMsg("get html has exception");
            responseData.setStatus(OpenPageConstant.STATUS_ONE);
            return responseData;
        }
    }

    @Override
    public ResponseData getJxBankCode(PocketSendCodeDto request) {
        ResponseData response = ResponseData.success();
        logger.info("get bank msg code,and param is:" + request.toString());
        try {
            if (redisTemplate.hasKey(OpenPageConstant.POCKET_GETCODE + request.getUserId())) {
                logger.info("redis time is not miss" + request.getUserId());
                ResponseData r = ResponseData.error("点击过快,请稍等2分钟");
                r.setData(null);
                return r;
            } else {
                redisTemplate.opsForValue().set(OpenPageConstant.POCKET_GETCODE + request.getUserId(), "1", 120, TimeUnit.SECONDS);
            }
            //通过userId查询用户信息
            ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(request.getUserId());
            if (!OpenPageConstant.STATUS_ZERO.equals(userInfo.getStatus())) {
                logger.error("select user error,and userId is:" + request.getUserId());
                return ResponseData.error("select user error");
            }
            UserInfo data = userInfo.getData();
            ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(request.getOrderNo());
            if (!OpenPageConstant.STATUS_ZERO.equals(orderByOrderNo.getStatus())) {
                logger.error("select order error,and userId is:" + request.getUserId());
                return ResponseData.error("select order error");
            }
            OrderInfo orderInfo = orderByOrderNo.getData();
            ResponseData<List<BankInfo>> bankInfosByBankAccout = userBankContract.getBankInfosByBankAccout(orderInfo.getBankAccount());
            if (!OpenPageConstant.STATUS_ZERO.equals(bankInfosByBankAccout.getStatus())) {
                logger.error("select bank error,and userId is:" + request.getUserId());
                return ResponseData.error("select bank error");
            }
            List<BankInfo> bankInfos = bankInfosByBankAccout.getData();
            BankInfo bankInfo = bankInfos.get(0);
            String mobile = bankInfo.getReservedPhone();
            OpenJxHtmlRequest htmlRequest = new OpenJxHtmlRequest();
            htmlRequest.setIdCardNumber(data.getIdNumber());
            htmlRequest.setBankCardNumber(orderInfo.getBankAccount());
            htmlRequest.setUserId(request.getUserId());
            //进行江西银行开户
            Map<String, Object> map = new HashMap<>(50);
            UUID uuid = UUID.randomUUID();
            map.put("driverUuid", uuid);
            map.put("userId", request.getUserId());
            ResponseData<String> string = getHtml(request.getUserId(),mobile);
            if (!OpenPageConstant.STATUS_ZERO.equals(string.getStatus())) {
                logger.error("get bank html url error,and param is:" + request.toString());
                return ResponseData.error("get bank msg code has exception");
            }
            openPage(string.getData(), htmlRequest, uuid);
            return response.setData(map);
        } catch (Exception e) {
            logger.error("get bank msg code has exception,and param is:" + request.toString(), e);
            return ResponseData.error("get bank msg code has exception");
        }

    }

    @Override
    public ResponseData submitJxMsgCode(SubmitJxMsgCode request) {
        ResponseData responseData = ResponseData.success();
        try {
            logger.info("begin submit msg code for open page,and param is:" + request.toString());
            if (request == null || StringUtils.isBlank(request.getSmsCode()) || StringUtils.isBlank(request.getDriverUuid())) {
                return ResponseData.error(OpenPageConstant.PRARM_ERROR);
            }
            Map<String, Object> map = new HashMap<>(50);
            map.put("code", request.getSmsCode());
            map.put("userId", request.getUserId());
            redisTemplate.opsForValue().set(OpenPageConstant.OPEM_PAGE_REDIS_CODE + request.getDriverUuid(), JSON.toJSONString(map), 10, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(OpenPageConstant.OPEM_PAGE_ING + request.getUserId(), JSON.toJSONString(map), 5, TimeUnit.MINUTES);
            return responseData;
        } catch (Exception e) {
            logger.error("submit msg code for open page has exception,and param is:" + request.toString(), e);
            return ResponseData.error("设置收款银行卡不成功!");
        }
    }

    @Override
    public ResponseData selectUserOpenDetail(PocketAccountDetailDto dto, boolean ifAgain) {
        logger.info("select user pocket open account detail,and param is:" + dto.toString());
        try {
            Map<String, String> map = new HashMap<>(100);
            //通过userId查询最新的订单
            ResponseData<List<OrderInfo>> lastOrderByUserId = orderContract.getLastOrderByUserId(dto.getUserId());
            OrderInfo orderInfo = null;
            if (OpenPageConstant.STATUS_ZERO.equals(lastOrderByUserId.getStatus())) {
                List<OrderInfo> orders = lastOrderByUserId.getData();
                if (orders != null && orders.size() > 0) {
                    orderInfo = orders.get(0);
                } else {
                    return ResponseData.error("can not find order");
                }
            }
            if (redisTemplate.hasKey(OpenPageConstant.ERROR_ORDER + dto.getUserId())) {
                //该订单已进入异常状态,需要重推其他渠道

                if (ifAgain || (!ifAgain && FundSourceEnum.KDLC.getCode().equals(orderInfo.getFundCode()))) {
                    orderInfo.setFundCode(FundSourceEnum.DLD.getCode());
                    orderContract.updateOrderInfo(orderInfo);
                    sendCapital(orderInfo);
                    map.put("stage", PocketAccountEnum.Open_Account_Fail.getCode());
                    redisTemplate.delete(OpenPageConstant.ERROR_ORDER + dto.getUserId());
                    return ResponseData.success(map);
                }
            }

            //需要查询数据库,查询用户在口袋理财的开户进度
            UserPocket userPocket = userPocketService.selectPocketUserByUserId(dto.getUserId());
            if (userPocket == null) {
                map.put("stage", PocketAccountEnum.Not_Open_Account.getCode());
                return ResponseData.success(map);
            }
            if (StringUtils.isNotBlank(userPocket.getMemberId())) {
                map.put("stage", PocketAccountEnum.Open_Account.getCode());
                return ResponseData.success(map);
            }

            //查询口袋理财用户的开户情况
            ResponseData<UserInfo> userInfo = userIdentityContract.getUserInfo(dto.getUserId());
            if (!OpenPageConstant.STATUS_ZERO.equals(userInfo.getStatus())) {
                logger.error("select user error,and userId is:" + dto.getUserId());
                return ResponseData.error("select user info error");
            }
            UserInfo data = userInfo.getData();
            PocketQueryAccountOpenDetailDto detailDto = new PocketQueryAccountOpenDetailDto();
            detailDto.setIdNumber(data.getIdNumber());
            ResponseData<PocketParentResult> responseData = pocket2Service.queryAccountOpenDetail(detailDto);
            if (!OpenPageConstant.STATUS_ZERO.equals(responseData.getStatus())) {
                logger.error(responseData.getMsg() + "and param is:" + detailDto.toString());
                return responseData;
            }
            PocketParentResult result = responseData.getData();
            if (!OpenPageConstant.STATUS_ZERO.equals(result.getRetCode())) {
                map.put("stage", PocketAccountEnum.Not_Open_Account.getCode());
                return ResponseData.success(map);
            }
            String retData = result.getRetData();
            PocketOpenAccountVo accountVo = JSONObject.parseObject(retData, PocketOpenAccountVo.class);
            if (PocketAccountEnum.Open_Account.getCode().equals(accountVo.getIsBindCard())) {
                //已开户的进行用户数据的保存
                userPocket.setMemberId(accountVo.getAccountId());
                userPocket.setUserId(data.getUserId());
                userPocket.setMobile(data.getAccountNumber());
                userPocket.setStage(Integer.valueOf(PocketAccountEnum.Open_Account.getCode()));
                userPocketService.update(userPocket);
                map.put("stage", PocketAccountEnum.Open_Account.getCode());
                return ResponseData.success(map);
            } else {
                if (redisTemplate.hasKey(OpenPageConstant.OPEM_PAGE_ING + dto.getUserId())){
                    map.put("stage", PocketAccountEnum.Open_Account_Ing.getCode());
                    return ResponseData.success(map);
                }
                map.put("stage", PocketAccountEnum.Not_Open_Account.getCode());
                return ResponseData.success(map);
            }
        } catch (Exception e) {
            logger.error("select user pocket open account detail has exception,and param is:" + dto.toString(), e);
            return ResponseData.error("select user pocket open account detail has exception");
        }

    }

    @Override
    public String pocketCallback(Pocket2CallbackDto dto) {
        logger.info("pocket callback ,and param is:" + dto.toString());
        boolean verifySign = KdaiSignUtils.verifySign(JSONObject.parseObject(JSON.toJSONString(dto)), pocketConfig.getPocketSignKey());
        if (!verifySign) {
            return "sign fail";
        }
        Map<String, Integer> map = new HashMap<>(100);
        map.put("code", 0);
        String jsonString = JSON.toJSONString(map);
        try {
            String txCode = dto.getTxCode();
            PocketTxCodeEnum txCodeEnum = PocketTxCodeEnum.toEnum(txCode);
            if (txCodeEnum == null) {
                logger.error("can not find match api,and txCode is:" + txCode);
                return "fail";
            }
            boolean fail = false;
            if (!OpenPageConstant.STATUS_ZERO.equals(dto.getRetCode())) {
                fail = true;
            }
            String retData = dto.getRetData();
            Pocket2CallbackDataVo dataVo = JSONObject.parseObject(retData, Pocket2CallbackDataVo.class);
            String orderId = dataVo.getOrderId();
            String status = dataVo.getStatus();
            //发送消息进行处理
//            PocketCallbackMessage message = new PocketCallbackMessage();
//            message.setTxCode(txCode);
//            message.setOrderId(orderId);
//            message.setStatus(status);
//            message.setRetMsg(dto.getRetMsg());
//            message.setFail(fail);
//            logger.info("send callback  mq,and message is:" + message.toString());
//            producer.sendMsg(message);
            return jsonString;
        } catch (Exception e) {
            logger.error("pocket callback has exception,and param is:" + dto.toString());
            return "fail";
        }
    }

    @Override
    public String pocketJob(Pocket2JobDto dto) {
        if (dto ==null || StringUtils.isBlank(dto.getTaskType())){
            return OpenPageConstant.PRARM_ERROR;
        }
        logger.info("begin pocket job,and param is:" +dto.toString());
        if ("1".equals(dto.getTaskType())) {
            pocketTask.loanJob();
        }
        if ("2".equals(dto.getTaskType())) {
            pocketTask.withdrawalJob();
        }
        return "success";
    }

    /**
     * 获取验证码
     *
     * @param orderInfo
     */
    private void sendCapital(OrderInfo orderInfo) {
        new Thread(() -> {
            OrderMessage message = new OrderMessage();
            message.setUserId(orderInfo.getUserId());
            message.setOrderNo(orderInfo.getOrderNo());
            message.setFundCode(orderInfo.getFundCode());
            if (orderInfo.getChannel() == null) {
                orderInfo.setChannel(0);
            }
            message.setChannel(orderInfo.getChannel());
            capitalService.newSendCapital(message, true);
        }).start();
    }

    /**
     * 获取验证码
     *
     * @param url
     * @param request
     * @param uuid
     */
    private void openPage(String url, OpenJxHtmlRequest request, UUID uuid) {
        new Thread(() -> {
            try {
                logger.info("begin open page thread,and url is:" + url);
                ResponseData responseData = crawlerUtil.getUrl(url);
                if (!OpenPageConstant.STATUS_ZERO.equals(responseData.getStatus())) {
                    return;
                }
                WebDriver dr = (WebDriver) responseData.getData();
                Thread.sleep(2000);
                //进入江西银行开户页面
                //获取绑定银行卡号DOM
                logger.info("Jiangxi bank account opening page source code is:" + dr.getPageSource());
                WebElement cardNo = dr.findElement(By.id("BIND_CARD_NO"));
                //设置银行卡号
                cardNo.sendKeys(request.getBankCardNumber());
                WebElement idNo = dr.findElement(By.id("IDNO"));
                //设置身份证号码
                idNo.sendKeys(request.getIdCardNumber());
                //通过密码生成工具生成密码并加密保存到数据库
                String password = userPocketService.selectPasswordByUserId(request.getUserId());
                if (StringUtils.isBlank(password)) {
                    password = PasswordUtil.getPassword();
                    BASE64Encoder encoder = new BASE64Encoder();
                    String base64Res = encoder.encode(password.getBytes());
                    //需要加密保存到数据库
                    UserPocket userPocket = new UserPocket();
                    userPocket.setUserId(request.getUserId());
                    userPocket.setPassword(base64Res);
                    userPocket.setMobile(request.getMobile());
                    try {
                        logger.info("begin save pocket password,and param is:" + userPocket.toString());
                        userPocketService.savePassword(userPocket);
                    } catch (Exception e) {
                        logger.error("save pocket password has exception");
                        return;
                    }
                } else {
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] buffer = decoder.decodeBuffer(password);
                    if (password == null) {
                        return;
                    }
                    password = new String(buffer);
                }
                //设置交易密码
                WebElement encPin1 = dr.findElement(By.id("encPin1"));
                //设置身份证号码
                encPin1.sendKeys(password);
                //设置确认交易密码
                WebElement encPin2 = dr.findElement(By.id("encPin2"));
                //设置身份证号码
                encPin2.sendKeys(password);
                WebElement getSms = null;
                try {
                    getSms = dr.findElement(By.id("smsBtn"));
                } catch (Throwable e) {
                    logger.error("can not find appGetSmsCode try another", e);
                    getSms = dr.findElement(By.id("appGetSmsCode"));
                }
                //得到获取验证码DOM后进行点击动作
                getSms.click();
                mapBean.getConcurrentHashMap().putIfAbsent(uuid.toString(), dr);
                logger.info(request.getUserId() + "click confirm success");
            } catch (Throwable e) {
                logger.error("headless browser to open page has exception,url:" + url, e);
                redisTemplate.opsForValue().set(OpenPageConstant.ERROR_ORDER + request.getUserId(), "1", 300, TimeUnit.MINUTES);
            }
        }).start();

    }

}
