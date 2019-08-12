package com.nyd.application.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nyd.application.api.AgreeMentContract;
import com.nyd.application.model.enums.AgreementType;
import com.nyd.application.model.enums.AppType;
import com.nyd.application.model.enums.ChannelCodeType;
import com.nyd.application.model.request.AgreementTemplateModel;
import com.nyd.application.model.request.ExTSignAutoModel;
import com.nyd.application.model.request.GenerateContractModel;
import com.nyd.application.model.request.MuBanRequestModel;
import com.nyd.application.service.AgreeMentService;
import com.nyd.application.service.FadadaService;
import com.nyd.application.service.commonEnum.ResultCode;
import com.nyd.application.service.consts.ApplicationConsts;
import com.nyd.application.service.util.FadadaApi;
import com.nyd.application.service.util.MongoApi;
import com.nyd.application.service.util.QiniuApi;
import com.nyd.application.service.validation.Verify;
import com.nyd.member.api.MemberLogContract;
import com.nyd.user.api.UserAccountContract;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserInfo;
import com.nyd.user.model.dto.AccountDto;
import com.tasfe.framework.redis.RedisService;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by hwei on 2017/11/25.
 */
@Service(value = "agreeMentContract")
public class AgreeMentServiceImpl implements AgreeMentService,AgreeMentContract {
    private static Logger LOGGER = LoggerFactory.getLogger(AgreeMentServiceImpl.class);

    //初始化网签公司名字
    private String companyName = ApplicationConsts.COMPANY_CLASS_NAME.companyNameNewUpToDateA;
    
    @Autowired
    FadadaService fadadaService;
    @Autowired
    QiniuApi qiniuApi;
    @Autowired
    MongoApi mongoApi;
    @Autowired
    FadadaApi fadadaApi;
    @Autowired(required = false)
    UserIdentityContract userIdentityContract;
    @Autowired
    RedisService redisService;
    @Autowired(required = false)
    private UserAccountContract userAccountContract;
    @Autowired(required = false)
    private UserSourceContract userSourceContract;
    @Autowired(required = false)
    private MemberLogContract memberLogContract;

    @Autowired
    private AgreeMentContract agreeMentContract;


    /**
     * 上传模板
     * @param agreementTemplateModel
     * @return
     */
    @Override
    public ResponseData uploadAgreemengt(AgreementTemplateModel agreementTemplateModel) {
        ResponseData responseData = ResponseData.success();
        if (!Verify.AgreementTemplateModelverify(agreementTemplateModel)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        //生成模板编号
        String templateId = agreementTemplateModel.getType()+System.currentTimeMillis();
        //根据七牛的key获取url
        try {
            String url = qiniuApi.downloadAgreeMentTempalte(agreementTemplateModel.getKey());
            LOGGER.error("上传到七牛云的合同的url : "+url);
            MuBanRequestModel muBanRequestModel = new MuBanRequestModel(templateId,url,null);
            responseData = fadadaService.uploadPdfTemplate(muBanRequestModel);
            LOGGER.error("fadada 返回值 : "+ responseData);
            LOGGER.error("fadada 返回值 : "+responseData.getStatus());
            if ("0".equals(responseData.getStatus())) {
                //上传成功
                Map<String,Object> map = new HashMap<>();
                if("nyd".equals(agreementTemplateModel.getAppName())){
                    map.put("contractId","contract");
                } else {
                    map.put("contractId","contract_"+agreementTemplateModel.getAppName());
                }
                map.put(agreementTemplateModel.getType(),templateId);
                try {
                    Integer contract_template = mongoApi.upsertContractId(map, "contract_template");
                    LOGGER.error("mongoApi 插入操作 : "+contract_template);
                } catch (Exception e) {
                    LOGGER.error("AgreeMentServiceImpl uploadAgreemengt upsert mongo except,request param is "+new Gson().toJson(agreementTemplateModel),e);
                    return ResponseData.error("合同模板编号存mongo失败");
                }
            }
            return responseData;
        } catch (Exception e) {
            LOGGER.error("AgreeMentServiceImpl uploadAgreemengt has except,request param is "+new Gson().toJson(agreementTemplateModel),e);
            return ResponseData.error("获取七牛url错误");
        }
    }

    /**
     * 生成自动还款协议并签章 功能
     * @param bankInfo
     * @return
     */
    @Override
    public ResponseData signAutoRepay(BankInfo bankInfo) {
        ResponseData responseData = ResponseData.success();
        if (!Verify.BankInfoverify(bankInfo)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(bankInfo));
       // 获取身份证号
        UserInfo userInfo = getUserInfo(bankInfo.getUserId());
        if (userInfo!=null&&userInfo.getIdNumber()!=null) {
            jsonObject .put("idNumber",userInfo.getIdNumber());
        } else {
            return ResponseData.error("没有获取到身份证号");
        }
        send(bankInfo,jsonObject);
        return responseData;
    }

    /**
     * 签署其他协议并签章（）
     * @param jsonObject
     * @return
     */
    @Override
    public ResponseData signOtherAgreeMent(JSONObject jsonObject) {
        //所有参数放在jsonObject
        sendOtherAgreement(jsonObject);
        return ResponseData.success();
    }

    /**
     * 签署会员协议
     * @param userId
     * @return
     */
    @Override
    public ResponseData signMemberAgreeMent(String userId) {
        ResponseData responseData = ResponseData.success();
        if (StringUtils.isBlank(userId)) {
            responseData = ResponseData.error("userId不能为空");
            return responseData;
        }
        sendMemberAgreeMent(userId);
        return responseData;
    }

    /**
     * 签署充值协议
     * @param jsonObject
     * @return
     */
    @Override
    public ResponseData signRechargeAgreeMent(JSONObject jsonObject) {
        sendRechargeAgreement(jsonObject);
        return ResponseData.success();
    }

    @Override
    public ResponseData signRegisterAgreeMent(JSONObject jsonObject) {
        sendRegisterAgreeMent(jsonObject);
        return ResponseData.success();
    }

    /**
     * 还款签章签协议--tds
     * @param jsonObject
     * @return
     */
    public ResponseData signConductLoan(JSONObject jsonObject){
        sendConductLoanAgreeMent(jsonObject);
        return ResponseData.success();
    }

    /**
     * 签署代扣协议
     * @param jsonObject
     * @return
     */
    @Override
    public ResponseData signDaiKouAgreeMent(JSONObject jsonObject) {
        sendDaiKouAgreeMent(jsonObject);
        return ResponseData.success();
    }

    /**
     * 新合同
     * @param bankInfo
     * @return
     * @author lk
     */
    @Override
    public ResponseData signAddressListAgreeMent(BankInfo bankInfo) {
    	ResponseData responseData = ResponseData.success();
        if (!Verify.BankInfoverify(bankInfo)) {
            responseData.setStatus(ResultCode.REQUEST_PRARM_EXCEPTION.getCode());
            responseData.setMsg(ResultCode.REQUEST_PRARM_EXCEPTION.getMessage());
            return responseData;
        }
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(bankInfo));
       // 获取身份证号
        UserInfo userInfo = getUserInfo(bankInfo.getUserId());
        if (userInfo!=null&&userInfo.getIdNumber()!=null) {
            jsonObject .put("idNumber",userInfo.getIdNumber());
        } else {
            return ResponseData.error("没有获取到身份证号");
        }
        sendChannelClass(bankInfo,jsonObject);
        return responseData;
    }

    /**
     * 前端查看签章协议接口
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public ResponseData getSignAgreement(String userId, String orderId) {
        ResponseData responseData = ResponseData.success();
        LOGGER.info("begin to getSignAgreement!orderId is "+orderId);
        Map<String,Object> map = new HashMap<>();
        String appFlag = "";
        if(StringUtils.isNotBlank(userId)){
            String appName = getLastAppName(userId);
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            // 查询隐私协议 和注册协议
            try {
                String Login = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.Login.getType()+userId+appFlag);
                map.put("login",Login);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement elecAuthorization has except,userId is "+userId);
            }
            try {
                String prip = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.Prip.getType()+userId+appFlag);
                map.put("prip",prip);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement elecAuthorization has except,userId is "+userId);
            }
        }
        if(StringUtils.isNotBlank(orderId)){
            //五个协议
            try {
                String electronic = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.Electronic.getType()+orderId+appFlag);
                map.put("electronic",electronic);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement elecAuthorization has except,orderId is "+orderId);
            }
            try {
                String server = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.Server.getType()+orderId+appFlag);
                map.put("server",server);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement serverProtocol has except,orderId is "+orderId);
            }
            try {
                String autoRepay = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.AutorRpay.getType()+orderId+appFlag);
                map.put("autoRepay",autoRepay);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement serverProtocol has except,orderId is "+orderId);
            }
            try {
                String zhunru = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.ZhunRu.getType()+orderId+appFlag);
                map.put("zhunru",zhunru);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement serverProtocol has except,orderId is "+orderId);
            }
            try {
                String geren = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.GeRen.getType()+orderId+appFlag);
                map.put("geren",geren);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement serverProtocol has except,orderId is "+orderId);
            }
            try {
                String creditApproval = fadadaApi.getClientExtra().invokeViewPdfURL(AgreementType.CreditApproval.getType()+orderId+appFlag);
                map.put("creditApproval",creditApproval);
            } catch (Exception e) {
                LOGGER.error("AgreeMentServiceImpl getSignAgreement serverProtocol has except,orderId is "+orderId);
            }
        }
        responseData.setData(map);
        return responseData;
    }

    //组装各个合同的参数
    private JSONObject getParam(JSONObject jsonObject,String type) {
        if (AgreementType.Electronic.getType().equals(type)) { //电子签名授权协议书
            JSONObject electronic = new JSONObject();
            //组装参数
            electronic.put("userName1",jsonObject.getString("userName"));
            electronic.put("userName2",jsonObject.getString("userName"));
            electronic.put("companyName",companyName);
            electronic.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return electronic;
        }
        if (AgreementType.Credit.getType().equals(type)) {
            JSONObject credit = new JSONObject();
            //组装参数
            credit.put("userName1",jsonObject.getString("userName"));
            credit.put("userName2",jsonObject.getString("userName"));
            credit.put("idNumber",jsonObject.getString("idNumber"));
            credit.put("companyName",companyName);
            credit.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return credit;
        }
        if (AgreementType.Server.getType().equals(type)) {
            JSONObject server = new JSONObject();
            //组装参数
            server.put("orderNo",jsonObject.getString("orderNo"));
            server.put("idNumber",jsonObject.getString("idNumber"));
            server.put("mobile",jsonObject.getString("mobile"));
            server.put("userName1",jsonObject.getString("userName"));
            server.put("loanMoney",jsonObject.getString("loanMoney"));
            server.put("loanDay",jsonObject.getString("loanDay"));
            server.put("loanRate1",jsonObject.getString("loanRate"));
            server.put("loanInterst",jsonObject.getString("loanInterst"));
            server.put("userName2",jsonObject.getString("userName"));
            server.put("loanUse",jsonObject.getString("loanUse"));
            server.put("loanRate2",jsonObject.getString("loanRate2"));
            server.put("fee1",jsonObject.getString("fee1"));
            server.put("fee2",jsonObject.getString("fee2"));
            server.put("companyName",companyName);
            server.put("companyCode","91230603MA1BF4298U");
            String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
            server.put("time1",date);
            server.put("time2",date);
            return server;
        }
        if (AgreementType.AutorRpay.getType().equals(type)) {  //自动还款服务协议
            JSONObject autorRpay = new JSONObject();
            //组装参数
            autorRpay.put("userName1",jsonObject.getString("userName"));
            autorRpay.put("bankNumber1",jsonObject.getString("bankAccount"));
            autorRpay.put("idNumber",jsonObject.getString("idNumber"));
            autorRpay.put("mobile",jsonObject.getString("mobile"));
            autorRpay.put("companyName",companyName);
            autorRpay.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return autorRpay;
        }
        if (AgreementType.Mortgage.getType().equals(type)) {  //抵押合同
            JSONObject mortgage = new JSONObject();
            //组装参数
            mortgage.put("contractId1",AgreementType.Mortgage.getType()+jsonObject.getString("orderNo"));
            mortgage.put("userName1",jsonObject.getString("userName"));
            mortgage.put("idNumber1",jsonObject.getString("idNumber"));
            mortgage.put("contractId2",AgreementType.Mortgage.getType()+jsonObject.getString("orderNo"));
            mortgage.put("loanMoney",jsonObject.getString("loanMoney"));
            mortgage.put("deviceId",jsonObject.getString("deviceId"));
            mortgage.put("mortgageAmount",jsonObject.getString("loanMoney"));
            mortgage.put("companyName2",companyName);
            mortgage.put("userName2",jsonObject.getString("userName"));
            mortgage.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return mortgage;
        }
        if (AgreementType.MemberServer.getType().equals(type)) {//会员合同
            JSONObject mem = new JSONObject();
            mem.put("userName",jsonObject.getString("userName"));
            mem.put("companyName",companyName);
            mem.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return mem;
        }
        if (AgreementType.Recharge.getType().equals(type)) {
            JSONObject recharge = new JSONObject();
            //组装参数
            recharge.put("companyName",companyName);
            recharge.put("userName",jsonObject.getString("userName"));
            recharge.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return recharge;
        }
        if (AgreementType.Login.getType().equals(type)) {
            JSONObject login = new JSONObject();
            //组装参数
            login.put("companyName1",companyName);
            login.put("userName",jsonObject.getString("userName"));
            login.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return login;
        }
        if (AgreementType.Prip.getType().equals(type)) {
            JSONObject prip = new JSONObject();
            //组装参数
            prip.put("companyName",companyName);
            prip.put("userName",jsonObject.getString("userName"));
            prip.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return prip;
        }
        if (AgreementType.DaiKou.getType().equals(type)) {
            JSONObject daikou = new JSONObject();
            //组装参数
            daikou.put("companyName",companyName);
            daikou.put("userName",jsonObject.getString("userName"));
            daikou.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return daikou;
        }
		if (AgreementType.ZhunRu.getType().equals(type)) {
            JSONObject ZhunRu = new JSONObject();
            //组装参数
            ZhunRu.put("companyName1",companyName);
            ZhunRu.put("userName",jsonObject.getString("userName"));
            ZhunRu.put("fee",jsonObject.getString("fee"));
            ZhunRu.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return ZhunRu;
        }
        if (AgreementType.GeRen.getType().equals(type)) {
            JSONObject GeRen = new JSONObject();
            //组装参数
            GeRen.put("idNumber",jsonObject.getString("idNumber"));
            GeRen.put("userName",jsonObject.getString("userName"));
            GeRen.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return GeRen;
        }
        if (AgreementType.CreditApproval.getType().equals(type)) {
            JSONObject credit = new JSONObject();
            //组装参数
            //甲方(借款人):username1
            credit.put("username1",jsonObject.getString("userName"));
            //身份证号:idNumber
            credit.put("idNumber",jsonObject.getString("idNumber"));
            //联系电话:mobile
            credit.put("mobile",jsonObject.getString("mobile"));
            //联系地址:address
            credit.put("address",jsonObject.getString("address"));
            //乙方(顾问方):username2
            credit.put("username2",companyName);
            //机构代码:companyCode
            credit.put("companyCode","91230603MA1BF4298U");
            //支付费用:fee
            credit.put("fee",jsonObject.getString("fee"));
            //甲方(签章):username1
            credit.put("username1",jsonObject.getString("userName"));
            //乙方(签章):companyName
            credit.put("companyName",companyName);
            //签约日期:time
            credit.put("time",new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
            return credit;
        }
        return null;
    }

    //获取用户信息
    private UserInfo getUserInfo(String userId) {
        ResponseData<UserInfo> responseData = userIdentityContract.getUserInfo(userId);
        if (responseData!=null&&"0".equals(responseData.getStatus())) {
            UserInfo userInfo = responseData.getData();
            return  userInfo;
        }
        return null;
    }

    public void send(BankInfo bankInfo,JSONObject jsonObject) {
        new Thread(() -> {
            String appName = getLastAppName(bankInfo.getUserId());
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.AutorRpay.getType())) {
                //生成合同
                GenerateContractModel autorepay = new GenerateContractModel();
                autorepay.setDocTitle("自动还款服务协议");
                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.AutorRpay.getType())));
                autorepay.setContractId(AgreementType.AutorRpay.getType()+bankInfo.getBankAccount()+appFlag);
                autorepay.setFontSize("9");
                autorepay.setFontType("0");
                autorepay.setParameterMap(getParam(jsonObject,AgreementType.AutorRpay.getType()));
                ResponseData responseData = fadadaService.generateContract(autorepay);
                LOGGER.info("生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.AutorRpay.getType()+bankInfo.getBankAccount()+appFlag);
                    exTSignAutoModel.setDocTitle("自动还款服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl signAutoRepay signResult is "+ JSONObject.toJSONString(signResult));
                }
            } else {
                LOGGER.error("AgreeMentServiceImpl signAutoRepay has except! 没有自动还款协议模板");
            }
        }).start();
    }

    public void sendOtherAgreement(JSONObject jsonObject) {
        new Thread(() -> {
            String appName = getLastAppName(jsonObject.getString("userId"));
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            //逐份生成合同并签章
            //电子签名授权协议书
            if (map.containsKey(AgreementType.Electronic.getType())) {
                //生成合同
                GenerateContractModel autorepay = new GenerateContractModel();
                autorepay.setDocTitle("电子签名授权协议书");
                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.Electronic.getType())));
                autorepay.setContractId(AgreementType.Electronic.getType()+jsonObject.getString("orderNo")+appFlag);
                autorepay.setFontSize("10");
                autorepay.setFontType("0");
                autorepay.setParameterMap(getParam(jsonObject,AgreementType.Electronic.getType()));
                ResponseData responseData = fadadaService.generateContract(autorepay);
                LOGGER.info("电子签名授权协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Electronic.getType()+jsonObject.getString("orderNo")+appFlag);
                    exTSignAutoModel.setDocTitle("电子签名授权协议书");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendOtherAgreement 电子签名授权协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
//            //征信查询授权书
//            if (map.containsKey(AgreementType.Credit.getType())) {
//                //生成合同
//                GenerateContractModel autorepay = new GenerateContractModel();
//                autorepay.setDocTitle("征信查询授权书");
//                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.Credit.getType())));
//                autorepay.setContractId(AgreementType.Credit.getType()+jsonObject.getString("orderNo")+appFlag);
//                autorepay.setFontSize("12");
//                autorepay.setFontType("0");
//                autorepay.setParameterMap(getParam(jsonObject,AgreementType.Credit.getType()));
//                ResponseData responseData = fadadaService.generateContract(autorepay);
//                LOGGER.info("征信查询授权书 生成合同结果是："+JSONObject.toJSONString(responseData));
//                if (responseData!=null&&"0".equals(responseData.getStatus())) {
//                    //去签章
//                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
//                    exTSignAutoModel.setContractId(AgreementType.Credit.getType()+jsonObject.getString("orderNo"));
//                    exTSignAutoModel.setDocTitle("征信查询授权书");
//                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
//                    exTSignAutoModel.setClientRole("1");
//                    exTSignAutoModel.setSignKeyword("上海造艺网络科技有限公司");
//                    exTSignAutoModel.setKeyWordStrategy("2");
//                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
//                    LOGGER.info("AgreeMentServiceImpl sendOtherAgreement 征信查询授权书 is "+ JSONObject.toJSONString(signResult));
//                }
//            }
//            借款居间服务协议
            if (map.containsKey(AgreementType.Server.getType())) {
                //生成合同
                GenerateContractModel autorepay = new GenerateContractModel();
                autorepay.setDocTitle(AgreementType.Server.getDescription());
                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.Server.getType())));
                autorepay.setContractId(AgreementType.Server.getType()+jsonObject.getString("orderNo")+appFlag);
                autorepay.setFontSize("10");
                autorepay.setFontType("0");
                autorepay.setParameterMap(getParam(jsonObject,AgreementType.Server.getType()));
                ResponseData responseData = fadadaService.generateContract(autorepay);
                LOGGER.info("借款居间服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Server.getType()+jsonObject.getString("orderNo")+appFlag);
                    exTSignAutoModel.setDocTitle(AgreementType.Server.getDescription());
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendOtherAgreement 借款居间服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }

            if (map.containsKey(AgreementType.Mortgage.getType())) {
                //生成合同
                GenerateContractModel autorepay = new GenerateContractModel();
                autorepay.setDocTitle("抵押合同");
                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.Mortgage.getType())));
                autorepay.setContractId(AgreementType.Mortgage.getType()+jsonObject.getString("orderNo")+appFlag);
                autorepay.setFontSize("10");
                autorepay.setFontType("0");
                autorepay.setParameterMap(getParam(jsonObject,AgreementType.Mortgage.getType()));
                ResponseData responseData = fadadaService.generateContract(autorepay);
                LOGGER.info("抵押合同 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Mortgage.getType()+jsonObject.getString("orderNo")+appFlag);
                    exTSignAutoModel.setDocTitle("抵押合同");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendOtherAgreement 抵押合同 is "+ JSONObject.toJSONString(signResult));
                }
            }

            //发送签署会员协议（只有会员费付费成功的才去签署会员协议）
            if (StringUtils.isNotBlank(jsonObject.getString("userId"))) {
                try {
                    ResponseData responseData = agreeMentContract.signMemberAgreeMent(jsonObject.getString("userId"));
                    if (responseData!=null&&"0".equals(responseData.getStatus())) {
                        LOGGER.info("评估及推荐服务协议签署成功，userId is "+jsonObject.getString("userId"));
                    }
                } catch (Exception e) {
                    LOGGER.error("评估及推荐服务协议签署错误，userId is "+jsonObject.getString("userId"),e);
                }
                //签署代扣协议
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("memberId","daikou"+jsonObject.getString("userId"));
                jsonObject1.put("accountNumber",jsonObject.getString("mobile"));
                jsonObject1.put("userId",jsonObject.getString("userId"));
                try {
                    ResponseData daikouRes = agreeMentContract.signDaiKouAgreeMent(jsonObject1);
                    LOGGER.info("签署代扣协议成功，userId is "+jsonObject1.getString("userId"));
                } catch (Exception e) {
                    LOGGER.error("代扣协议签署错误，userId is "+jsonObject1.getString("userId"),e);
                }
            }

        }).start();
    }


    public void sendMemberAgreeMent(String userId){
        new Thread(() -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",userId);
            UserInfo userInfo = getUserInfo(userId);
            if (userInfo!=null&&StringUtils.isNotBlank(userInfo.getIdNumber())) {
                jsonObject .put("idNumber",userInfo.getIdNumber());
            }
            if (userInfo!=null&&StringUtils.isNotBlank(userInfo.getRealName())) {
                jsonObject .put("userName",userInfo.getRealName());
            }
            String appName = getLastAppName(jsonObject.getString("userId"));
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.MemberServer.getType())){
                //生成合同
                GenerateContractModel mem = new GenerateContractModel();
                mem.setDocTitle("评估及推荐服务协议");
                mem.setTemplateId(String.valueOf(map.get(AgreementType.MemberServer.getType())));
                String contractId = "m"+jsonObject.getString("userId")+System.currentTimeMillis();
                mem.setContractId(contractId);
                mem.setFontSize("10");
                mem.setFontType("0");
                mem.setParameterMap(getParam(jsonObject,AgreementType.MemberServer.getType()));
                ResponseData responseData = fadadaService.generateContract(mem);
                LOGGER.info("评估及推荐服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(contractId);
                    exTSignAutoModel.setDocTitle("评估及推荐服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendMemberAgreeMent 评估及推荐服务协议 is "+ JSONObject.toJSONString(signResult));
                }
                try {
                    redisService.setString(AgreementType.MemberServer.getType()+userId+appFlag,contractId);
                } catch (Exception e) {
                    LOGGER.error("redis set has exception!",e);
                }
            }
            if (map.containsKey(AgreementType.Recharge.getType())) {
                //生成合同
                GenerateContractModel recharge = new GenerateContractModel();
                recharge.setDocTitle("充值协议");
                recharge.setTemplateId(String.valueOf(map.get(AgreementType.Recharge.getType())));
                String contractId = "r"+jsonObject.getString("userId")+System.currentTimeMillis();
                recharge.setContractId(contractId);
                recharge.setFontSize("10");
                recharge.setFontType("0");
                recharge.setParameterMap(getParam(jsonObject,AgreementType.Recharge.getType()));
                ResponseData responseData = fadadaService.generateContract(recharge);
                LOGGER.info("充值协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(contractId);
                    exTSignAutoModel.setDocTitle("充值协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRechargeAgreement 充值协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
        }).start();
    }

    private void sendRechargeAgreement(JSONObject jsonObject) {
        new Thread(() -> {
            String appName = getLastAppNameByMobile(jsonObject.getString("accountNumber"));
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.Recharge.getType())) {
                //生成合同
                GenerateContractModel recharge = new GenerateContractModel();
                recharge.setDocTitle("充值协议");
                recharge.setTemplateId(String.valueOf(map.get(AgreementType.Recharge.getType())));
                recharge.setContractId(AgreementType.Recharge.getType()+jsonObject.getString("cashNo")+appFlag);
                recharge.setFontSize("10");
                recharge.setFontType("0");
                recharge.setParameterMap(getParam(jsonObject,AgreementType.Recharge.getType()));
                ResponseData responseData = fadadaService.generateContract(recharge);
                LOGGER.info("充值协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Recharge.getType()+jsonObject.getString("cashNo")+appFlag);
                    exTSignAutoModel.setDocTitle("充值协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRechargeAgreement 充值协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
        }).start();
    }

    private void sendConductLoanAgreeMent(JSONObject jsonObject){
        new Thread(() ->{
//            String appName = getLastAppName(jsonObject.getString("userId"));
        	 String appName="xxd";
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.ZhunRu.getType())) {
                //生成准入风险批核及借款推荐协议
                GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("准入风险批核及借款推荐服务协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.ZhunRu.getType())));
                login.setContractId(AgreementType.ZhunRu.getType()+jsonObject.getString("orderId" )+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.ZhunRu.getType()));

                LOGGER.info("准入风险批核及借款推荐服务协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("准入风险批核及借款推荐服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.ZhunRu.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("准入风险批核及借款推荐服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 准入风险批核及借款推荐服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            if (map.containsKey(AgreementType.Electronic.getType())) {
                //生成电子签名授权协议
                GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("电子签名授权协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.Electronic.getType())));
                login.setContractId(AgreementType.Electronic.getType()+jsonObject.getString("orderId" )+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.Electronic.getType()));

                LOGGER.info("电子签名授权协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("电子签名授权协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Electronic.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("电子签名授权协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 电子签名授权协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            if (map.containsKey(AgreementType.GeRen.getType())) {
                //生成个人信息查询及使用授权协议
                GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("个人信息查询及使用授权协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.GeRen.getType())));
                login.setContractId(AgreementType.GeRen.getType()+jsonObject.getString("orderId" )+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.GeRen.getType()));

                LOGGER.info("个人信息查询及使用授权协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("个人信息查询及使用授权协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.GeRen.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("个人信息查询及使用授权协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
//                    exTSignAutoModel.setSignKeyword("大庆市语诗铭科技有限公司");
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 个人信息查询及使用授权协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            if (map.containsKey(AgreementType.AutorRpay.getType())) {
                //生成自动还款服务协议
                GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("自动还款服务协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.AutorRpay.getType())));
                login.setContractId(AgreementType.AutorRpay.getType()+jsonObject.getString("orderId")+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.AutorRpay.getType()));

                LOGGER.info("自动还款服务协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("自动还款服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.AutorRpay.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("自动还款服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 自动还款服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            if (map.containsKey(AgreementType.Server.getType())) {
                //生成借款居间服务协议
            	GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("借款居间服务协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.Server.getType())));
                login.setContractId(AgreementType.Server.getType()+jsonObject.getString("orderId" )+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.Server.getType()));

                LOGGER.info("借款居间服务协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("借款居间服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Server.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("借款居间服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 借款居间服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            //生成信用批核服务协议
            if (map.containsKey(AgreementType.CreditApproval.getType())) {
                //初始化信用批核服务协议
            	GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("信用批核服务协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.CreditApproval.getType())));
                login.setContractId(AgreementType.CreditApproval.getType()+jsonObject.getString("orderId" )+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.CreditApproval.getType()));

                LOGGER.info("信用批核服务协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("信用批核服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.CreditApproval.getType()+jsonObject.getString("orderId")+appFlag);
                    exTSignAutoModel.setDocTitle("信用批核服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 信用批核服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }

        }).start();
    }

    private void sendRegisterAgreeMent(JSONObject jsonObject) {
        new Thread(() -> {
//            String appName = getLastAppName(jsonObject.getString("userId"));
        	
        	String appName ="xxd";
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.Login.getType())) {
                //生成合同
                GenerateContractModel login = new GenerateContractModel();
                login.setDocTitle("平台用户注册服务协议");
                login.setTemplateId(String.valueOf(map.get(AgreementType.Login.getType())));
                login.setContractId(AgreementType.Login.getType()+jsonObject.getString("userId")+appFlag);
                login.setFontSize("10");
                login.setFontType("0");
                login.setParameterMap(getParam(jsonObject,AgreementType.Login.getType()));

                LOGGER.info("生成注册服务协议，请求参数{}"+jsonObject.toJSONString(login));
                ResponseData responseData = fadadaService.generateContract(login);
                LOGGER.info("平台用户注册服务协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Login.getType()+jsonObject.getString("userId")+appFlag);
                    exTSignAutoModel.setDocTitle("平台用户注册服务协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 平台用户注册服务协议 is "+ JSONObject.toJSONString(signResult));
                }
            }
            if (map.containsKey(AgreementType.Prip.getType())) {
                //生成合同
                GenerateContractModel prip = new GenerateContractModel();
                prip.setDocTitle("隐私政策");
                prip.setTemplateId(String.valueOf(map.get(AgreementType.Prip.getType())));
                prip.setContractId(AgreementType.Prip.getType()+jsonObject.getString("userId")+appFlag);
                prip.setFontSize("10");
                prip.setFontType("0");
                prip.setParameterMap(getParam(jsonObject,AgreementType.Prip.getType()));
                LOGGER.info("生成隐私政策协议，请求参数{}"+jsonObject.toJSONString(prip));
                ResponseData responseData = fadadaService.generateContract(prip);
                LOGGER.info("隐私政策 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.Prip.getType()+jsonObject.getString("userId")+appFlag);
                    exTSignAutoModel.setDocTitle("隐私政策");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendRegisterAgreeMent 隐私政策 is "+ JSONObject.toJSONString(signResult));
                }
            }
        }).start();
    }

    private void sendDaiKouAgreeMent(JSONObject jsonObject) {
        new Thread(() -> {
            UserInfo userInfo = getUserInfo(jsonObject.getString("userId"));
            if (userInfo!=null&&StringUtils.isNotBlank(userInfo.getRealName())) {
                jsonObject .put("userName",userInfo.getRealName());
            }
            String appName = getLastAppNameByMobile(jsonObject.getString("accountNumber"));
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.DaiKou.getType())) {
                //生成合同
                GenerateContractModel recharge = new GenerateContractModel();
                recharge.setDocTitle("代扣协议");
                recharge.setTemplateId(String.valueOf(map.get(AgreementType.DaiKou.getType())));
                recharge.setContractId(AgreementType.DaiKou.getType()+jsonObject.getString("userId")+appFlag);
                recharge.setFontSize("10");
                recharge.setFontType("0");
                recharge.setParameterMap(getParam(jsonObject,AgreementType.DaiKou.getType()));
                ResponseData responseData = fadadaService.generateContract(recharge);
                LOGGER.info("代扣协议 生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.DaiKou.getType()+jsonObject.getString("userId")+appFlag);
                    exTSignAutoModel.setDocTitle("代扣协议");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl sendDaiKouAgreeMent 代扣协议 is "+ JSONObject.toJSONString(signResult));
                }
            }

        }).start();
    }
    
    //新合同网签根据-->银行渠道    changjie 常捷  xunlian 讯联or新渠道 去做不同的网签
    public void sendChannelClass(BankInfo bankInfo,JSONObject jsonObject) {
        new Thread(() -> {
        	String paramFind = "";
    		String channelCode = bankInfo.getChannelCode();
        	//根据银行渠道网签abc三家网签合同
        	if(StringUtils.isNotBlank(channelCode)) {
        		paramFind = ChannelCodeType.getCompany(channelCode).getCompanyName();
        	}
        	
            String appName = getLastAppName(bankInfo.getUserId());
            String appFlag = "";
            if (StringUtils.isNotBlank(appName)) {
                appFlag = getDesByName(appName);
            }
            //获取模板
            Map<String,Object> map = mongoApi.getContractTemplate(appName);
            LOGGER.info("map is "+JSONObject.toJSONString(map));
            if (map.containsKey(AgreementType.AutorRpay.getType())) {
                //生成合同
                GenerateContractModel autorepay = new GenerateContractModel();
                autorepay.setDocTitle("通信录信息使用授权书");
                autorepay.setTemplateId(String.valueOf(map.get(AgreementType.AutorRpay.getType())));
                autorepay.setContractId(AgreementType.AutorRpay.getType()+bankInfo.getBankAccount()+appFlag);
                autorepay.setFontSize("9");
                autorepay.setFontType("0");
                autorepay.setParameterMap(getParam(jsonObject,AgreementType.AutorRpay.getType()));
                ResponseData responseData = fadadaService.generateContract(autorepay);
                LOGGER.info("生成合同结果是："+JSONObject.toJSONString(responseData));
                if (responseData!=null&&"0".equals(responseData.getStatus())) {
                    //去签章
                    ExTSignAutoModel exTSignAutoModel = new ExTSignAutoModel();
                    exTSignAutoModel.setContractId(AgreementType.AutorRpay.getType()+bankInfo.getBankAccount()+appFlag);
                    exTSignAutoModel.setDocTitle("通信录信息使用授权书");
                    exTSignAutoModel.setTransactionId(UUID.randomUUID().toString().replaceAll("-", ""));
                    exTSignAutoModel.setClientRole("1");
                    exTSignAutoModel.setSignKeyword(companyName);
                    exTSignAutoModel.setKeyWordStrategy("2");
                    ResponseData signResult =fadadaService.extSignAuto(exTSignAutoModel);
                    LOGGER.info("AgreeMentServiceImpl signAutoRepay signResult is "+ JSONObject.toJSONString(signResult));
                }
            } else {
                LOGGER.error("AgreeMentServiceImpl signAutoRepay has except! 没有新合同模板");
            }
        }).start();
    }

    private String getLastAppName(String userId) {
        try {
            ResponseData<AccountDto> responseData = userAccountContract.getAccount(userId);
            if (responseData!=null&&"0".equals(responseData.getStatus())) {
                AccountDto accountDto = responseData.getData();
                if (accountDto!=null&&StringUtils.isNotBlank(accountDto.getAccountNumber())) {
                    ResponseData res = userSourceContract.selectUserSourceByMobile(accountDto.getAccountNumber());
                    if (res!=null&&"0".equals(res.getStatus())) {
                        LoginLog loginLog = (LoginLog) res.getData();
                        if(loginLog!=null) {
                            return loginLog.getAppName();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("getLastAppName has error,userId is "+userId,e);
        }
        return null;
    }

    private String getLastAppNameByMobile(String mobile) {
        if (StringUtils.isNotBlank(mobile)){
            try {
                ResponseData res = userSourceContract.selectUserSourceByMobile(mobile);
                if (res!=null&&"0".equals(res.getStatus())) {
                    LoginLog loginLog = (LoginLog) res.getData();
                    if (loginLog!=null) {
                        return loginLog.getAppName();
                    }
                }
            } catch (Exception e) {
                LOGGER.error("getLastAppName has error,mobile is "+mobile,e);
            }
        }
        return null;
    }

    private String getDesByName(String appName) {
        if (StringUtils.isBlank(appName)) {
            return "";
        }
        for (AppType appType :AppType.values()) {
            if (appType.getType().equals(appName)) {
                return appType.getDescription();
            }
        }
        return "";
    }

}
