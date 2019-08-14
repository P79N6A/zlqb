package com.nyd.user.service.impl;

import java.text.Bidi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nyd.order.model.common.ChkUtil;
import com.nyd.user.dao.BankDao;
import com.nyd.user.dao.StepDao;
import com.nyd.user.entity.Step;
import com.nyd.user.model.vo.IdentityVo;
import com.nyd.user.service.consts.UserConsts;
import com.nyd.zeus.api.zzl.ZeusForLXYService;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.api.zzl.hnapay.HnaPayPaymentService;
import com.nyd.zeus.api.zzl.xunlian.XunlianPayService;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.hnapay.req.HnaPayConfirmReq;
import com.nyd.zeus.model.hnapay.req.HnaPayContractReq;
import com.nyd.zeus.model.hnapay.resp.HnaPayConfirmResp;
import com.nyd.zeus.model.hnapay.resp.HnaPayContractResp;
import com.nyd.zeus.model.xunlian.req.IdentifyauthVO;
import com.nyd.zeus.model.xunlian.req.XunlianSignVO;
import com.nyd.zeus.model.xunlian.resp.IdentifyResp;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.dao.UserBindDao;
import com.nyd.user.dao.mapper.UserBindMapper;
import com.nyd.user.entity.UserBind;
import com.nyd.user.model.BankInfo;
import com.nyd.user.model.UserBindCardConfirm;
import com.nyd.user.model.UserBindCardReq;
import com.nyd.user.model.UserInfo;
import com.nyd.user.service.BankInfoService;
import com.nyd.user.service.BindCardService;
import com.nyd.user.service.util.RestTemplateApi;
import com.nyd.user.service.util.UserProperties;
import com.tasfe.framework.support.model.ResponseData;

import javax.json.JsonObject;

/**
 * @author zhangdk
 */
@Service(value = "bindCardService")
public class BindCardServiceImpl implements BindCardService {
    private static Logger LOGGER = LoggerFactory.getLogger(BindCardServiceImpl.class);

    @Autowired
    RestTemplateApi restTemplateApi;
    @Autowired
    UserProperties userProperties;
    @Autowired
    private UserIdentityContract userIdentityContract;
    @Autowired
    private UserBindDao userBindDao;
    @Autowired
    private BankInfoService bankInfoService;
    @Autowired
    private UserBindMapper userBindMapper;

    @Autowired
    private ZeusForOrderPayBackServise zeusForOrderPayBackServise;

    @Autowired
    private StepDao stepDao;

    @Autowired
    private BankDao bankDao;

    @Autowired
    private XunlianPayService xunlianPayService;

    @Autowired
    private HnaPayPaymentService hnaPayPaymentService;
    /**
     * 银行卡验证获取验证码
     */
    @Override
    public ResponseData bindCard(UserBindCardReq req) {


        if (StringUtils.isEmpty(req.getCardNo()) || StringUtils.isEmpty(req.getPhone())
                || StringUtils.isEmpty(req.getUserId())) {
            return ResponseData.error("请求参数异常！请重试");
        }

        LOGGER.info("+++++++用户ID：" + req.getUserId());
        UserInfo user = userIdentityContract.getUserInfo(req.getUserId()).getData();
        LOGGER.info("+++++++用户信息：" + JSON.toJSONString(user));
        if (null == user|| StringUtils.isEmpty(user.getRealName())) {
            return ResponseData.error("没有查询到用户信息！");
        }
        
        LOGGER.info("+++++++预绑卡请求参数：" + JSON.toJSONString(req));
        //判断渠道
        if(ChkUtil.isEmptys(req.getChannelCode())){
            return  ResponseData.error("获取渠道异常");
        }else{
            try {
                if ("xunlian".equals(req.getChannelCode())) {
                    //调用讯联 预绑卡
                    IdentifyauthVO vo = new IdentifyauthVO();
                    vo.setName(user.getRealName());
                    vo.setAccount(req.getCardNo());
                    vo.setOrganName(req.getBankName());
                    vo.setMobile(req.getPhone());
                    vo.setIdCode(user.getIdNumber());
                    LOGGER.info("讯联预绑卡请求参数:" + JSON.toJSONString(vo));
                    CommonResponse<IdentifyResp> reslut = xunlianPayService.sendMsg(vo);

                    LOGGER.info("讯联预绑卡响应参数:"+  JSON.toJSONString(reslut));
                    if ("T".equals(reslut.getData().getRetFlag()) && reslut.isSuccess()) {
                        //发送短信验证码成功
                        ResponseData resp = new ResponseData();
                        JSONObject json = new JSONObject();
                        json.put("requestno", reslut.getData().getMerOrderId());
                        json.put("smsSendNo",reslut.getData().getSmsSendNo());
                        json.put("channelCode",req.getChannelCode());
                        resp.setData(json);
                        insertUserBind(user, req, resp);
                        return resp.success(json);
                    } else {
                        //发送短信验证码失败
                        LOGGER.error("讯联请求绑卡发送短信验证码服务失败,订单号:{}",req.getUserId());
                        return ResponseData.error("讯联请求绑卡发送短信验证码服务异常，请稍后处理！");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("讯联请求绑卡发送短信验证码服务失败，异常信息：{}", e.getMessage());
                e.printStackTrace();
            }
            try {
                if ("xinsheng".equals(req.getChannelCode())) {
                    LOGGER.error("新生请求绑卡发送短信验证码服务失败,订单号:{}",req.getUserId());
                    HnaPayContractReq hnaPayContractReq = new HnaPayContractReq();
                    hnaPayContractReq.setCardNo(req.getCardNo());
                    hnaPayContractReq.setHolderName(user.getRealName());
                    hnaPayContractReq.setIdentityCode(req.getIdcardNo());
                    hnaPayContractReq.setMerOrderId(user.getUserId());
                    hnaPayContractReq.setMerUserId(user.getUserId());
                    hnaPayContractReq.setMobileNo(user.getAccountNumber());
                    //调用新生 预绑卡
                    LOGGER.error("调用新生 预绑卡,请求信息:{}",hnaPayContractReq);
                    CommonResponse<HnaPayContractResp> hnaPayContractRespCommonResponse = hnaPayPaymentService.contract(hnaPayContractReq);
                    LOGGER.error("调用新生 预绑卡,返回信息:{}",hnaPayContractRespCommonResponse);
                    if(hnaPayContractRespCommonResponse.isSuccess()){
                        ResponseData resp = new ResponseData();
                        JSONObject json = new JSONObject();
                        HnaPayContractResp hnaPayContractResp = hnaPayContractRespCommonResponse.getData();
                        json.put("requestno", hnaPayContractResp.getMerOrderId());
                        json.put("channelCode",req.getChannelCode());
                        resp.setData(json);
                        insertUserBind(user, req, resp);
                        return resp.success(json);
                    }else{
                        //发送短信验证码失败
                        LOGGER.error("新生请求绑卡发送短信验证码服务失败,订单号:{}",req.getUserId());
                        return ResponseData.error("新生请求绑卡发送短信验证码服务异常，请稍后处理！");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("新生请求绑卡发送短信验证码服务失败，异常信息：{}", e.getMessage());
                e.printStackTrace();
            }
        }
        LOGGER.info("畅捷用户绑卡发送短信验证码请求参数：" + JSON.toJSONString(req));
        // 查询客户基本信息及详情
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cardNo", req.getCardNo());
        param.put("idcardNo", user.getIdNumber());
        param.put("phone", req.getPhone());
        param.put("userId", req.getUserId());
        param.put("username", user.getRealName());
        param.put("bankName", req.getBankName());
        param.put("bankCode", req.getBankCode());
        param.put("source", req.getAppName());
        //汇聚支付绑卡统一使用nyd
        if("P".equals(req.getPayOrLoanFlag())) {
        	param.put("source", "nyd-repay");
        }
        if (StringUtils.isNotBlank(req.getPayChannelCode())) {
            param.put("payChannelCode", req.getPayChannelCode());
        }
        if (StringUtils.isNotBlank(req.getPayChannelCodeBx())) {
        	 param.put("payChannelCodeBx", req.getPayChannelCodeBx());
        }
        LOGGER.info("畅捷请求绑卡发送短信验证码服务参数： " + JSON.toJSONString(param));
        try {
            ResponseData resp = restTemplateApi.postForObject(userProperties.getUserBindCardUri(), param, ResponseData.class);
            LOGGER.info("畅捷请求绑卡发送短信验证码响应信息:" + JSON.toJSONString(resp));
            if (resp != null && !("0").equals(resp.getStatus())) {
                resp.setStatus("1");
            }
            req.setChannelCode("changjie"); 
            insertUserBind(user, req, resp);
            return resp;
        } catch (Exception e) {
            LOGGER.error("畅捷请求绑卡发送短信验证码服务失败，异常信息：{}", e.getMessage());
            return ResponseData.error("请求绑卡发送短信验证码服务异常，请稍后处理！");
        }
    }

    private void insertUserBind(UserInfo user, UserBindCardReq req, ResponseData resp) {

        UserBind bind = new UserBind();
        if (resp != null && resp.getData() != null) {
            String res = JSON.toJSONString(resp.getData());
            JSONObject re = JSONObject.parseObject(res);
            String reqNo = re.getString("requestno");
            bind.setSmsSendNo(re.getString("smsSendNo"));
            bind.setRequestNo(reqNo);
            bind.setChannelCode(re.getString("channelCode"));
        }
        bind.setCardNo(req.getCardNo());
        bind.setIdNumber(user.getIdNumber());
        bind.setUserId(req.getUserId());
        bind.setUserName(user.getRealName());
        bind.setSignStatus("1");
        bind.setBankCode(req.getBankCode());
        bind.setBankName(req.getBankName());
        bind.setPhone(req.getPhone());
        bind.setChannelCode(req.getChannelCode()); 
        LOGGER.info(JSON.toJSONString(bind));
        try {
            userBindDao.save(bind);
        } catch (Exception e) {
            LOGGER.error("插入t_user_bind表失败：{}", e.getMessage());
        }
    }

    /**
     * 银行卡验证确认验证码
     */
    @Override
    public ResponseData bindCardConfirm(UserBindCardConfirm req) {
        //判断渠道
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", req.getUserId());
//        params.put("channelCode", "changjie");
        List<UserBind> userBind = userBindMapper.selectBindInfo(params);
        UserBind userBindInfo = userBind.get(0);

        try {
            if(ChkUtil.isEmptys(userBindInfo.getChannelCode())){
                return  ResponseData.error("获取渠道异常");
            }else {
                if ("xunlian".equals(userBindInfo.getChannelCode())) {
                    //调用讯联 预绑卡  查询预绑卡流水号

                    if (userBind != null) {
                        XunlianSignVO vo = new XunlianSignVO();
                        vo.setSmsVerifyCode(req.getValidatecode());
                        vo.setMerOrderId(userBind.get(0).getRequestNo());
                        vo.setSmsSendNo(userBind.get(0).getSmsSendNo());
                        LOGGER.info("迅捷请求绑卡参数,{}" + JSON.toJSONString(vo));
                        CommonResponse<IdentifyResp> reslut = xunlianPayService.sign(vo);

                        LOGGER.info("迅捷响应参数,{}" + JSON.toJSONString(reslut));
                        if ("T".equals(reslut.getData().getRetFlag()) && reslut.isSuccess()) {
                            //绑卡成功
                            BankInfo bankInfo = new BankInfo();
                            UserBind bind = userBind.get(0);
                            bankInfo.setAccountName(bind.getUserName());
                            bankInfo.setAccountNumber(bind.getIdNumber());
                            bankInfo.setBankAccount(bind.getCardNo());
                            bankInfo.setBankName(bind.getBankName());
                            bankInfo.setReservedPhone(bind.getPhone());
                            bankInfo.setSoure(3);
                            bankInfo.setChannelCode(userBindInfo.getChannelCode());
                            bankInfo.setProtocolId(reslut.getData().getProtocolId());
                            bankInfo.setUserId(bind.getUserId());
                            bankDao.save(bankInfo);
                            LOGGER.info("迅捷更新t_user_bank表记录：" + JSON.toJSONString(bankInfo));


                            //更新信息完整度
                            LOGGER.info("begin to update stepInfo");
                            Step step = new Step();
                            step.setUserId(req.getUserId());
                            step.setBankFlag(UserConsts.FILL_FLAG);
                            stepDao.updateStep(step);

                            //如果是已有绑定卡，则返回成功
                            return ResponseData.success();

                        }else{
                            LOGGER.error("迅捷请求绑卡确认服务失败，异常信息：{}",JSON.toJSONString(reslut));
                            return ResponseData.error("请求确认绑卡服务异常，请稍后处理！");
                        }
                    }
                }
                if ("xinsheng".equals(userBindInfo.getChannelCode())){
                    HnaPayConfirmReq hnaPayConfirmReq = new HnaPayConfirmReq();
                    hnaPayConfirmReq.setSmsCode(req.getValidatecode());
                    hnaPayConfirmReq.setHnapayOrderId(userBindInfo.getUserId());
                    hnaPayConfirmReq.setMerOrderId(userBindInfo.getMerOrderId());
                    LOGGER.error("新生请求绑卡确认服务信息：{}",JSON.toJSONString(hnaPayConfirmReq));
                    CommonResponse<HnaPayConfirmResp> hnaPayConfirmRespCommonResponse = hnaPayPaymentService.confirm(hnaPayConfirmReq);
                    LOGGER.error("新生请求绑卡返回服务信息：{}",JSON.toJSONString(hnaPayConfirmRespCommonResponse));
                    if (hnaPayConfirmRespCommonResponse.isSuccess()){
                        //绑卡成功
                        HnaPayConfirmResp hnaPayConfirmResp = hnaPayConfirmRespCommonResponse.getData();
                        BankInfo bankInfo = new BankInfo();
                        UserBind bind = userBind.get(0);
                        bankInfo.setAccountName(bind.getUserName());
                        bankInfo.setAccountNumber(bind.getIdNumber());
                        bankInfo.setBankAccount(bind.getCardNo());
                        bankInfo.setBankName(bind.getBankName());
                        bankInfo.setReservedPhone(bind.getPhone());
                        bankInfo.setSoure(4);
                        bankInfo.setChannelCode(userBindInfo.getChannelCode());
//                        bankInfo.setProtocolId(reslut.getData().getProtocolId());
                        bankInfo.setUserId(bind.getUserId());
                        bankInfo.setBizProtocolNo(hnaPayConfirmResp.getBizProtocolNo());
                        bankInfo.setPayProtocolNo(hnaPayConfirmResp.getPayProtocolNo());
                        bankDao.save(bankInfo);
                        LOGGER.info("新生更新t_user_bank表记录：" + JSON.toJSONString(bankInfo));
                        //更新信息完整度
                        LOGGER.info("begin to update stepInfo");
                        Step step = new Step();
                        step.setUserId(req.getUserId());
                        step.setBankFlag(UserConsts.FILL_FLAG);
                        stepDao.updateStep(step);

                        //如果是已有绑定卡，则返回成功
                        return ResponseData.success();
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if ("xqj1".equals(req.getAppName())) {
            if (StringUtils.isEmpty(req.getRequestNo()) || StringUtils.isEmpty(req.getValidatecode())) {
                return ResponseData.error("畅捷请求参数异常！请重试");
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("requestNo", req.getRequestNo());
            param.put("validatecode", req.getValidatecode());
            try {
                ResponseData resp = restTemplateApi.postForObject(userProperties.getUserBindCardConfirmUri(), param, ResponseData.class);
                LOGGER.info("畅捷确认绑卡响应信息:" + JSON.toJSONString(resp));
                try {
                    userBindUpdate(req, resp);
                } catch (Exception ex) {
                    LOGGER.error("更新t_user_bank异常：" + ex.getMessage());
                }
                return resp;
            } catch (Exception e) {
                LOGGER.error("畅捷请求绑卡确认服务失败，异常信息：{}", e.getMessage());
                return ResponseData.error("畅捷请求确认绑卡服务异常，请稍后处理！");
            }
        }

        if (StringUtils.isEmpty(req.getRequestNo()) || StringUtils.isEmpty(req.getValidatecode()) || StringUtils.isEmpty(req.getBankCode()) || StringUtils.isEmpty(req.getBankName())) {
            return ResponseData.error("畅捷请求参数异常！请重试");
        }
        ResponseData bankList = getBankList(req.getAppName());
        if (bankList == null || bankList.getData() == null) {
            return ResponseData.error("畅捷查询支持银行列表服务失败");
        } else {
            String s = JSON.toJSONString(bankList);
            if (s.indexOf(req.getBankCode()) < 0) {
                if (s.indexOf(req.getBankName()) < 0) {
                    return ResponseData.error("请重新选择开户银行");
                }
            }
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("requestNo", req.getRequestNo());
        param.put("validatecode", req.getValidatecode());
        try {
            ResponseData resp = restTemplateApi.postForObject(userProperties.getUserBindCardConfirmUri(), param, ResponseData.class);
            LOGGER.info("畅捷确认绑卡响应信息:" + JSON.toJSONString(resp));
            try {
                userBindUpdate(req, resp);
            } catch (Exception ex) {
                LOGGER.error("畅捷更新t_user_bank异常：" + ex.getMessage());
            }
            return resp;
        } catch (Exception e) {
            LOGGER.error("畅捷请求绑卡确认服务失败，异常信息：{}", e.getMessage());
            return ResponseData.error("畅捷请求确认绑卡服务异常，请稍后处理！");
        }
    }

    /**
     * 查询对应appName的银行列表
     *
     * @param appName
     * @return
     */
    private ResponseData getBankList(String appName) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (appName == null) {
            param.put("appCode", "xxd");
        } else {
            param.put("appCode", appName);
        }
        LOGGER.info("查询银行卡列表参数： " + JSON.toJSONString(param));
        try {
            ResponseData resp = restTemplateApi.postForObject(userProperties.getUserCardListUri(), param, ResponseData.class);
            LOGGER.info("查询银行卡列表响应信息：{} ", JSON.toJSONString(resp));
            return resp;
        } catch (Exception e) {
            LOGGER.error("查询银行列表失败：{}", e.getMessage());
            return ResponseData.error();
        }
    }

    /**
     * 银行卡信息手动录入
     */
    @Override
    public ResponseData bindCardReset(List<UserBindCardConfirm> req) {
        try {
            for (UserBindCardConfirm request : req) {
                userBindUpdate(request);
            }
        } catch (Exception ex) {
            LOGGER.error("更新t_user_bank异常：" + ex.getMessage());
        }
        return ResponseData.success();
    }

    public void userBindUpdate(UserBindCardConfirm req, ResponseData resp) {
        if (resp.getStatus().equals("0")) {
            BankInfo info = new BankInfo();
            Map<String, String> param = new HashMap<String, String>();
            param.put("requestNo", req.getRequestNo());
			/*LOGGER.info("调用银行卡信息查询服务请求单号：" + req.getRequestNo());
			ResponseData respQuery = restTemplateApi.postForObject(userProperties.getUserBindCardAQueryUri(), param,ResponseData.class);
			String res = JSON.toJSONString(respQuery.getData());
			LOGGER.info("调用服务查询绑定银行卡信息：" + res);*/
            List<UserBind> userBind = userBindMapper.selectByUserIdAndCardNo(param);
            if (userBind != null) {
                // 查询客户基本信息及详情
				/*LOGGER.info("+++++++用户ID：" + req.getUserId());
				UserInfo user = userIdentityContract.getUserInfo(req.getUserId()).getData();
				LOGGER.info("+++++++用户信息：" + JSON.toJSONString(user));
				JSONObject bankInfos  = JSONObject.parseObject(res);*/
                UserBind bind = userBind.get(0);
                info.setAccountName(bind.getUserName());
                info.setAccountNumber(bind.getIdNumber());
                info.setBankAccount(bind.getCardNo());
                info.setBankName(bind.getBankName());
                info.setReservedPhone(bind.getPhone());
                info.setUserId(bind.getUserId());
                LOGGER.info("更新t_user_bank表记录：" + JSON.toJSONString(info));
                bankInfoService.saveBankInfoNoJudge(info);
            }
        }
    }

    public void userBindUpdate(UserBindCardConfirm req) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("requestNo", req.getRequestNo());
        LOGGER.info("调用银行卡信息查询服务请求单号：" + req.getRequestNo());
        ResponseData respQuery = restTemplateApi.postForObject(userProperties.getUserBindCardAQueryUri(), param, ResponseData.class);
        String res = JSON.toJSONString(respQuery.getData());
        LOGGER.info("调用服务查询绑定银行卡信息：" + res);
        if (res != null) {
            // 查询客户基本信息及详情
            BankInfo info = new BankInfo();
            LOGGER.info("+++++++用户ID：" + req.getUserId());
            UserInfo user = userIdentityContract.getUserInfo(req.getUserId()).getData();
            LOGGER.info("+++++++用户信息：" + JSON.toJSONString(user));
            JSONObject bankInfos = JSONObject.parseObject(res);
            info.setAccountName(user.getRealName());
            info.setAccountNumber(bankInfos.getString("cardNo"));
            info.setBankAccount(bankInfos.getString("cardNo"));
            info.setBankName(bankInfos.getString("bankName"));
            info.setReservedPhone(bankInfos.getString("phone"));
            info.setUserId(req.getUserId());
            LOGGER.info("更新t_user_bank表记录：" + JSON.toJSONString(info));
            bankInfoService.saveBankInfoNoJudge(info);
        }
    }


    /**
     * 获取绑卡渠道
     * @return
     */
    public JSONObject queryBindCardChannelCode(String userId){

        JSONObject jsonObject = new JSONObject();

        LOGGER.info("请求获取渠道用户id{}"+userId);

        String channleCode = "";
        try {
        	
            channleCode = zeusForOrderPayBackServise.getPaychannel();
            LOGGER.info("获取绑卡渠道用户id{}，渠道{}:"+userId,channleCode);
            jsonObject.put("channelCode",channleCode);
            if(ChkUtil.isEmptys(channleCode)){
                LOGGER.error("获取绑卡渠道异常,响应参数:{}",channleCode);

                jsonObject.put("status","1");
                return jsonObject;
            }
            jsonObject.put("status","0");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取绑卡渠道异常,{}",e.getMessage());
        }
        return jsonObject;
    }
}
