package com.nyd.capital.ws.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.api.service.RemitService;
import com.nyd.capital.model.kzjr.*;
import com.nyd.capital.model.vo.KzjrOpenAccountVo;
import com.nyd.capital.service.Contants;
import com.nyd.capital.service.impl.KzjrFundService;
import com.nyd.capital.service.kzjr.KzjrOpenPageService;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.thread.GetSmsRunnable;
import com.nyd.capital.service.validate.ValidateUtil;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
@RestController
@RequestMapping("/capital/kzjr")
public class KzjrController {
    Logger logger = LoggerFactory.getLogger(KzjrController.class);
    @Autowired
    private KzjrService kzjrService;
    @Autowired
    private KzjrConfig kzjrConfig;
    @Autowired
    private RemitService remitService;
    @Autowired
    private KzjrFundService kzjrFundService;
    @Autowired
    private ValidateUtil validateUtil;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private OrderContract orderContract;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private KzjrOpenPageService kzjrOpenPageService;

    @Autowired
    private UserAccountContract userAccountContract;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;





    @RequestMapping(value = "/getSmsCode")
    public ResponseData getSmsCode(@RequestBody OpenPageInfo openPageInfo){



        ResponseData result = ResponseData.success();
        JSONObject jsonObject = new JSONObject();
        //
        logger.info("获取短信验证码的请求信息"+JSON.toJSONString(openPageInfo));

        if(openPageInfo.getChannel()!=null&&BorrowConfirmChannel.YMT.getChannel() ==openPageInfo.getChannel()){
            logger.info("来自银码头"+openPageInfo.getAccountNumber());
            List<String> accounts = new ArrayList<>();
            accounts.add(openPageInfo.getAccountNumber());
            ResponseData<Set<String>> userIds = userAccountContract.queryAccountByAccountList(accounts);
            logger.info("根据account查询nyd userId结果为" + JSON.toJSONString(userIds));
            // 无法读取到用户
            if ("1".equals(userIds.getStatus()) || userIds.getData() == null || userIds.getData().size() == 0) {
                logger.error("getSmsCode error huo qu userid" + openPageInfo.getAccountNumber() );

                jsonObject.put("type","9");
                result.setMsg("银码头获取用户id失败");
                return result;
            }

            String userId = userIds.getData().iterator().next();
            openPageInfo.setUserId(userId);
        }
        GetSmsRunnable runnable = new GetSmsRunnable(kzjrOpenPageService,openPageInfo,orderContract,redisTemplate);
        threadPoolTaskExecutor.execute(runnable);
        return ResponseData.success();
    }

    @RequestMapping(value = "/querySmsStatus")
    public ResponseData querySmsStatus(@RequestBody SmsStatusInfo smsStatusInfo){
        logger.info("查询短信状态"+JSON.toJSONString(smsStatusInfo));
        if(smsStatusInfo.getChannel()!=null&&BorrowConfirmChannel.YMT.getChannel() ==smsStatusInfo.getChannel()){
            logger.info("查询短信状态来自银码头"+smsStatusInfo.getAccountNumber());
            List<String> accounts = new ArrayList<>();
            accounts.add(smsStatusInfo.getAccountNumber());
            ResponseData<Set<String>> userIds = userAccountContract.queryAccountByAccountList(accounts);
            logger.info("querySmsStatus查询nyd userId结果为" + JSON.toJSONString(userIds));
            // 无法读取到用户
            if ("1".equals(userIds.getStatus()) || userIds.getData() == null || userIds.getData().size() == 0) {
                logger.error("submitSmsCode error huo qu userid" + smsStatusInfo.getAccountNumber() );

                return ResponseData.error("系统异常,稍后再试");
            }

            String userId = userIds.getData().iterator().next();
            smsStatusInfo.setUserId(userId);
        }

        String result = (String) redisTemplate.opsForValue().get(Contants.KZJR_SMS_STATUS+smsStatusInfo.getUserId());
        logger.info("从redis获取的sms状态为"+result);
        ResponseData responseData = ResponseData.success();
        JSONObject object = new JSONObject();
        if(result==null){
            object.put("type","0");//waiting

        }else {
            if("2".equals(result)){
                object.put("type","2");
            }else if(result.startsWith("1")){
                object.put("type","1");
                if(smsStatusInfo.getChannel()!=null&&BorrowConfirmChannel.YMT.getChannel() ==smsStatusInfo.getChannel()){

                }else {
                    object.put("orderNo",result.split("_")[1]);
                }
            }else {
                object.put("type",result);
            }
        }

        responseData.setData(object);
        logger.info(smsStatusInfo.getUserId()+"查询的sms状态结果"+JSON.toJSONString(responseData));
        return responseData;

    }

    @RequestMapping(value = "/submitSmsCode")
    public ResponseData submitSmsCode(@RequestBody SubmitSmsInfo submitSmsInfo){
        logger.info("提交短信验证码的请求"+JSON.toJSONString(submitSmsInfo));

        if(submitSmsInfo.getChannel()!=null&&BorrowConfirmChannel.YMT.getChannel() ==submitSmsInfo.getChannel()){
            logger.info("提交来自银码头"+submitSmsInfo.getAccountNumber());
            List<String> accounts = new ArrayList<>();
            accounts.add(submitSmsInfo.getAccountNumber());
            ResponseData<Set<String>> userIds = userAccountContract.queryAccountByAccountList(accounts);
            logger.info("根据account查询nyd userId结果为" + JSON.toJSONString(userIds));
            // 无法读取到用户
            if ("1".equals(userIds.getStatus()) || userIds.getData() == null || userIds.getData().size() == 0) {
                logger.error("submitSmsCode error huo qu userid" + submitSmsInfo.getAccountNumber() );

                return ResponseData.error("系统异常,稍后再试");
            }

            String userId = userIds.getData().iterator().next();
            submitSmsInfo.setUserId(userId);
        }

        return kzjrOpenPageService.submitSmsCode(submitSmsInfo);
    }


    @RequestMapping(value = "/callback" ,produces="text/plain;charset=UTF-8")
    public String callback(@RequestBody String callback){
        logger.info("回调信息"+callback);
        try {
            kzjrFundService.saveLoanResult(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 调用空中金融的中间页面
     *
     * @param response HttpServletResponse
     * @param request  HttpServletRequest
     */
    @RequestMapping(value = "/openPage", produces = "text/plain;charset=UTF-8")
    public void openPage(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

        String loanAmount = request.getParameter("a");
        String name = null;
        String userId = request.getParameter("id");
        if (userId!=null){
            UserInfo userInfo = userIdentityContract.getUserInfo(userId).getData();
            name = userInfo.getRealName();
            logger.info("name" + name);
        }
        String idCard = request.getParameter("d");
        String mobile = request.getParameter("c");
        String channelStr = request.getParameter("b");  //判断是否 是 银码头或者 侬要贷
        String borrowTimeStr = request.getParameter("t");
        Integer channel = Integer.valueOf(channelStr);
        Integer borrowTime = Integer.valueOf(borrowTimeStr);
        logger.info("loanAmount" + loanAmount);
        logger.info("name" + name);
        logger.info("idCard" + idCard);
        logger.info("mobile" + mobile);
        logger.info("channel" + channel);
        logger.info("borrowTime" + borrowTime);
        logger.info("userId" + userId);


//        validateUtil.validate(vo);
        KzjrOpenAccountVo vo = new KzjrOpenAccountVo();
//        vo.setUserId(userId);
        vo.setName(name);
        vo.setMobile(mobile);
//        vo.setCardNo(bankAccount);
        vo.setIdType(1);
        vo.setIdNo(idCard);
        if (BorrowConfirmChannel.NYD.getChannel() == channel) {
            logger.info("侬要贷渠道" + kzjrConfig.getReturnUrl());
            vo.setReturnUrl(kzjrConfig.getReturnUrl());

        } else if (BorrowConfirmChannel.YMT.getChannel() == channel) {
            logger.info("ymt渠道" + kzjrConfig.getReturnUrlYmt());
            vo.setReturnUrl(kzjrConfig.getReturnUrlYmt() + "?loanAmount=" + loanAmount + "&borrowTime=" + borrowTime);
        }

        try {
            String page = remitService.accountOpenPage(vo);
            if (!page.contains("html")) {
                logger.info("统计已开户结果:"+mobile+":"+name+":"+channel+":"+page);
                PrintWriter out = response.getWriter();
                out.println("开户失败! ");
                out.close();

            } else {
                PrintWriter out = response.getWriter();
                out.println(page);
                out.close();
            }
        } catch (IOException e) {
            logger.error("get openHtml errer and result is " + JSON.toJSONString(request));
            e.printStackTrace();
        }


    }

    @RequestMapping(value = "/openReturn", produces = "text/plain;charset=UTF-8")
    public String accountOpenPageReturn() {

        return "accountOpenPageReturn";
    }

    @RequestMapping("/sendSmsCode")
    public ResponseData sendSmsCode() {
        SendSmsRequest request = new SendSmsRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());
        request.setBizType(1);
        request.setMobile("15618624753");
        return ResponseData.success(kzjrService.sendSmsCode(request));
    }

    @RequestMapping("/accountOpen")
    public ResponseData accountOpen() throws UnsupportedEncodingException {
        OpenAccountRequest request = new OpenAccountRequest();
        request.setChannelCode("20171211101081528645410816");
        request.setIdType(1);
        request.setIdNo("110101199001017524");
        request.setMobile("11108572654");
        request.setName("李测试账户4");
        request.setSmsCode("111111");
        request.setCardNo("6222620410005461772");

//        return ResponseData.success(kzjrService.accountOpen(request));
        return null;
    }

    @RequestMapping("/queryAccount")
    public ResponseData queryAccount() {
        QueryAccountRequest request = new QueryAccountRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());
        request.setIdType(1);
        request.setIdNo("220502199601013897");
        return ResponseData.success(kzjrService.queryAccount(request));
    }

    @RequestMapping(value = "/index", produces = "text/plain;charset=UTF-8")
    public String index() {
        return "欢迎index";
    }

    //资产提交
    @RequestMapping("/assetSubmit")
    public ResponseData assetSubmit(@RequestBody String orderid) {
        logger.info("##########" + orderid);
        AssetSubmitRequest request = new AssetSubmitRequest();
        request.setChannelCode("20171211101081528645410816");
        request.setAccountId("20171214102147578761732096");
//        request.setProductCode("20171212101373462634053632");
        request.setAmount(new BigDecimal("100.0"));
        request.setOrderId(orderid);
        request.setType(2);
        request.setDuration(14);

//        System.out.println(kzjrService.assetSubmit(request));

        return ResponseData.success(kzjrService.assetSubmit(request));
    }

    //获取渠道产品列表
    @RequestMapping("/productList")
    public ResponseData productList() {
        GetProductRequest request = new GetProductRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.productList(request));
    }

    //还款通知
    @RequestMapping("/repayNotify")
    public ResponseData repayNotify() {
        RepayNotifyRequest repayNotifyRequest = new RepayNotifyRequest();
        repayNotifyRequest.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.repayNotify(repayNotifyRequest));
    }

    //放款查询
    @RequestMapping("/queryMatchResult")
    public ResponseData queryMatchResult() {
        QueryRemitResult remitResult = new QueryRemitResult();
        remitResult.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.queryMatchResult(remitResult));
    }

    //放款批量查询
    @RequestMapping("/queryMatchResultList")
    public ResponseData queryMatchResultList() {
        QueryRemitResultBatch remitResultBatch = new QueryRemitResultBatch();
        remitResultBatch.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.queryMatchResultList(remitResultBatch));
    }

    //按天获取渠道列表
    @RequestMapping("/queryAssetList")
    public ResponseData queryAssetList() {
        QueryAssetListRequest listRequest = new QueryAssetListRequest();
        listRequest.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.queryAssetList(listRequest));
    }

    //查询单个资产
    @RequestMapping("/queryAsset")
    public ResponseData queryAsset() {
        QueryAssetRequest request = new QueryAssetRequest();
        request.setChannelCode(kzjrConfig.getChannelCode());

        return ResponseData.success(kzjrService.queryAsset(request));
    }

    //放款异步通知
    @RequestMapping("/remitNotify")
    public ResponseData remitNotify() {
        return ResponseData.success();
    }
}
