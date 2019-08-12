package com.nyd.capital.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyd.capital.api.service.RemitService;
import com.nyd.capital.model.dto.KzjrAccountDto;
import com.nyd.capital.model.enums.KzjrAccountStatus;
import com.nyd.capital.model.enums.ResultEnum;
import com.nyd.capital.model.kzjr.*;
import com.nyd.capital.model.response.KzjrStatusReponse;
import com.nyd.capital.model.vo.CheckStatusVo;
import com.nyd.capital.model.vo.KzjrOpenAccountVo;
import com.nyd.capital.model.vo.OpenAccountVo;
import com.nyd.capital.model.wt.WtCallbackResponse;
import com.nyd.capital.service.UserAccountService;
import com.nyd.capital.service.kzjr.KzjrService;
import com.nyd.capital.service.kzjr.config.KzjrConfig;
import com.nyd.capital.service.validate.ValidateUtil;
import com.nyd.order.model.enums.BorrowConfirmChannel;
import com.nyd.user.api.UserIdentityContract;
import com.nyd.user.model.UserInfo;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/14
 **/
@Service("remitService")
public class RemitServiceImpl implements RemitService , InitializingBean {

    Logger logger = LoggerFactory.getLogger(RemitServiceImpl.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private KzjrConfig kzjrConfig;

    @Autowired
    private ValidateUtil validateUtil;

    @Autowired
    private KzjrService kzjrService;

    @Autowired
    private UserIdentityContract userIdentityContract;

    @Autowired
    private WtFundService wtFundService;


    /**
     * 查询空中金融（对应背后的江西银行）的账户状态，进行开户操作、解绑请求短信验证码等
     *
     * @param vo CheckStatusVo
     * @return ResponseData<KzjrStatusReponse>
     */
    @Override
    public ResponseData<KzjrStatusReponse> checkStatusKzjr(CheckStatusVo vo) {
        logger.info("checkStatusKzjr的入参" + JSON.toJSONString(vo));
        validateUtil.validate(vo);
        UserInfo userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
        int i = 10;
        while (i > 0 && userInfo == null) { // 之前有出现过dubbo偶尔会断的情况
            userInfo = userIdentityContract.getUserInfo(vo.getUserId()).getData();
            i--;
        }
        if (userInfo == null) {
            logger.info("checkStatusKzjr方法获取的userInfo为null" + vo.getUserId());
            ResponseData responseData = ResponseData.error();
            responseData.setData(null);
            return responseData;
        }
        //查询 kzjr的用户信息
        KzjrAccountDto dto = userAccountService.queryInfo(vo.getUserId(), vo.getAccNo(), 1, userInfo.getIdNumber());

//        logger.info("查询的kzjr账户信息为"+dto);

        if (dto == null) {
            logger.info("查询的kzjr账户信息为queryInfo为null" + vo.getUserId());
            ResponseData responseData = ResponseData.error();
            responseData.setData(null);
            return responseData;
        }
        logger.info("查询的kzjr账户信息为" + JSON.toJSONString(dto));

        if (!vo.getIsFirst()) {
            if (dto.getStatus() == KzjrAccountStatus.NO_ACCOUNT) {
                logger.info(vo.getUserId() + ":" + vo.getMobile() + ":" + vo.getChannel() + "统计二次开户结果noaccount" + JSON.toJSONString(dto));

            } else if (dto.getStatus() == KzjrAccountStatus.NO_BANKACC) {
                logger.info(vo.getUserId() + ":" + vo.getMobile() + ":" + vo.getChannel() + "统计二次开户结果nobankacc" + JSON.toJSONString(dto));
            } else if (dto.getStatus() == KzjrAccountStatus.OPENING) {
                logger.info(vo.getUserId() + ":" + vo.getMobile() + ":" + vo.getChannel() + "统计二次开户结果openning" + JSON.toJSONString(dto));

            } else {
                logger.info(vo.getUserId() + ":" + vo.getMobile() + ":" + vo.getChannel() + "统计二次开户结果成功" + JSON.toJSONString(dto));
            }
        }
        KzjrStatusReponse reponse = new KzjrStatusReponse();

        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setMobile(vo.getMobile());
            request.setCardNo(vo.getAccNo());
            if (dto.getStatus() == KzjrAccountStatus.NO_ACCOUNT) { // 没有账户，需要打开页面进行注册
                logger.info("第一次查询后空中返回为未开户,用户手机号为：：" + vo.getMobile());
                reponse.setFlag(1);
                KzjrOpenAccountVo kzjrOpenAccountVo = new KzjrOpenAccountVo();
                kzjrOpenAccountVo.setName(userInfo.getRealName());
                kzjrOpenAccountVo.setMobile(vo.getMobile());
                kzjrOpenAccountVo.setIdType(1);
                kzjrOpenAccountVo.setIdNo(userInfo.getIdNumber());
                if (BorrowConfirmChannel.NYD.getChannel() == vo.getChannel()) {
                    logger.info("侬要贷渠道" + kzjrConfig.getReturnUrl());
                    kzjrOpenAccountVo.setReturnUrl(kzjrConfig.getReturnUrl());

                } else if (BorrowConfirmChannel.YMT.getChannel() == vo.getChannel()) {
                    logger.info("ymt渠道" + kzjrConfig.getReturnUrlYmt());
                    kzjrOpenAccountVo.setReturnUrl(kzjrConfig.getReturnUrlYmt() + "?loanAmount=" + vo.getLoanAmount() + "&borrowTime=" + vo.getBorrowTime());
                }
                //判断下是否造艺的老户
                String page = accountOpenPage(kzjrOpenAccountVo);
                if (!page.contains("html")) {
                    logger.info("查询江西银行返回的不是一个页面：page:" + JSON.toJSONString(page));
                    QueryAccountRequest request1 = new QueryAccountRequest();
                    request1.setIdType(1);
                    request1.setIdNo(userInfo.getIdNumber());
                    JSONObject resultObj = kzjrService.queryAccount(request1);
                    logger.info("再次查询空中的开户信息为：" + resultObj);
                    if (resultObj.getInteger("status") != 0) {
                        ResponseData responseData = ResponseData.error();
                        responseData.setData(null);
                        return responseData;
                    }
                    if (resultObj.getJSONObject("data") == null) {
                        reponse.setUrl(kzjrConfig.getOpenPageUrl() + "?a=" + vo.getLoanAmount() + "&n=" + URLEncoder.encode(userInfo.getRealName(), "UTF-8") + "&d=" + userInfo.getIdNumber() + "&c=" + vo.getMobile() + "&b=" + vo.getChannel() + "&t=" + vo.getBorrowTime() + "&id=" + vo.getUserId()); // 打开中间页面，跳转到空中金融的注册页面
                        ResponseData responseData = ResponseData.success();
                        responseData.setData(reponse);
                        logger.info("no account时 返回的信息为" + JSON.toJSONString(reponse));
                        return responseData;
                    }
                    Integer accountOpenStatus = resultObj.getJSONObject("data").getInteger("accountOpenStatus");
                    if (accountOpenStatus == 2) {
                        String accountId = resultObj.getJSONObject("data").getString("accountId");
                        UserKzjrInfo info = new UserKzjrInfo();
                        info.setAccountId(accountId);
                        info.setBankAccount(vo.getAccNo());
                        info.setUserId(vo.getUserId());
                        info.setStatus(0);
                        userAccountService.saveInfo(info); // 保存t_user_kzjr表信息
                        reponse.setFlag(2);
                        ResponseData responseData = ResponseData.success();
                        responseData.setData(reponse);
                        return responseData;
                    } else {
                        ResponseData responseData = ResponseData.error();
                        responseData.setData(null);
                        return responseData;
                    }
                } else {

                    reponse.setUrl(kzjrConfig.getOpenPageUrl() + "?a=" + vo.getLoanAmount() + "&n=" + URLEncoder.encode(userInfo.getRealName(), "UTF-8") + "&d=" + userInfo.getIdNumber() + "&c=" + vo.getMobile() + "&b=" + vo.getChannel() + "&t=" + vo.getBorrowTime() + "&id=" + vo.getUserId()); // 打开中间页面，跳转到空中金融的注册页面
                    ResponseData responseData = ResponseData.success();
                    responseData.setData(reponse);
                    logger.info("no account时 返回的信息为" + JSON.toJSONString(reponse));
                    return responseData;
                }
            } else if (dto.getStatus() == KzjrAccountStatus.NO_BANKACC) { // 银行卡不一致，需要解绑银行卡，需要发送验证码
                if (vo.getIsFirst()) {
                    try {
                        UnBindCardRequest unBindCardRequest = new UnBindCardRequest();
                        unBindCardRequest.setAccountId(dto.getP2pId());

                        kzjrService.unBindCard(unBindCardRequest);
                    } catch (Exception e) {
                        logger.error("解绑出现错误", e.getMessage());

                        ResponseData responseData = ResponseData.error();
                        responseData.setData(null);
                        return responseData;
                    }
//                logger.info("要删除银行卡号的表id为"+dto.getId());
//                userAccountService.updateBankAccById(dto.getId());

                    request.setBizType(2);

                    reponse.setP2pId(dto.getP2pId());
                    reponse.setFlag(0);

                    logger.info("发送短信验证码请求" + JSON.toJSONString(request));
                    JSONObject obj = kzjrService.sendSmsCode(request); // 发送短信验证码请求
                    logger.info("发送短信验证码结果" + JSON.toJSONString(obj));
                    if (obj.getInteger("status") == 0) {
                        ResponseData responseData = ResponseData.success();
                        responseData.setData(reponse);
                        return responseData;
//                    return ResponseData.success(result);
                    } else {
                        logger.error("kzjr发送短信验证码错误" + obj.toJSONString());
                        ResponseData responseData = ResponseData.error();
                        responseData.setData(null);
                        return responseData;

                    }
                } else {
                    reponse.setP2pId(dto.getP2pId());
                    reponse.setFlag(0);
                    ResponseData responseData = ResponseData.success();
                    responseData.setData(reponse);
                    return responseData;
                }
            } else if (dto.getStatus() == KzjrAccountStatus.OPENING) { // 正在开户中
                reponse.setFlag(3);
                ResponseData responseData = ResponseData.success();
                responseData.setData(reponse);
                return responseData;

            } else { // 成功
                reponse.setFlag(2);
                ResponseData responseData = ResponseData.success();
                responseData.setData(reponse);
                return responseData;
            }
        } catch (
                Exception e) {
            //e.printStackTrace();
            ResponseData responseData = ResponseData.error();
            responseData.setData(null);
            return responseData;
        }
//        return null;
    }

    @Override
    public ResponseData wtLoan(List<WtCallbackResponse> wtCallbackList) {
        if (wtCallbackList == null) {
            return ResponseData.error("传入的list为空");
        }

        for (WtCallbackResponse wtCallback : wtCallbackList) {
            try {
                boolean flag = wtFundService.saveLoanResult(JSON.toJSONString(wtCallback));
                if (flag) {
                    logger.info("稳通放款结果生成账单信息成功");
//               return ResponseData.success();
                } else {
                    logger.info("稳通放款结果生成账单信息失败" + JSON.toJSONString(wtCallback));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("稳通放款结果生成账单信息异常" + JSON.toJSONString(wtCallback), e);
            }
        }
        return ResponseData.success();
    }

//    @Override
//    public ResponseData<CheckSendSmsDto> checkIfSendSms(CheckSendSmsVo vo) {
//        try {
//            validateUtil.validate(vo);
//
//            KzjrAccountDto dto = userAccountService.queryInfo(vo.getUserId(), vo.getAccNo(),null,null);
//
//            SendSmsRequest request = new SendSmsRequest();
//            request.setChannelCode(kzjrConfig.getChannelCode());
//            request.setMobile(vo.getMobile());
//
//            CheckSendSmsDto result = new CheckSendSmsDto();
//            if (dto.getStatus() == KzjrAccountStatus.NO_ACCOUNT) {
//                request.setBizType(1);
//
//                result.setFlag(0);
//
//                JSONObject obj = kzjrService.sendSmsCode(request);
//
//                logger.info("*******" + obj.toJSONString());
//
//                if (obj.getInteger("status") == 0) {
//                    ResponseData responseData = ResponseData.success();
//                    responseData.setData(result);
//                    return responseData;
//                } else {
//                    ResponseData responseData = ResponseData.error();
//                    responseData.setData(null);
//                    return responseData;
//                }
//            } else if (dto.getStatus() == KzjrAccountStatus.NO_BANKACC) {
//                try {
//                    UnBindCardRequest unBindCardRequest = new UnBindCardRequest();
//                    unBindCardRequest.setChannelCode(kzjrConfig.getChannelCode());
//                    unBindCardRequest.setAccountId(dto.getP2pId());
//
//                    kzjrService.unBindCard(unBindCardRequest);
//                }catch (Exception e){
//                    logger.error("解绑出现错误",e.getMessage());
//                    e.printStackTrace();
//                    result.setP2pId(dto.getP2pId());
//                    result.setFlag(2);
//                    ResponseData responseData = ResponseData.success();
//                    responseData.setData(result);
//                    return responseData;
//                }
//                logger.info("要删除银行卡号的表id为"+dto.getId());
//                userAccountService.updateBankAccById(dto.getId());
//
//                request.setBizType(2);
//
//                result.setP2pId(dto.getP2pId());
//                result.setFlag(0);
//
//                JSONObject obj = kzjrService.sendSmsCode(request);
//                if (obj.getInteger("status") == 0) {
//                    ResponseData responseData = ResponseData.success();
//                    responseData.setData(result);
//                    return responseData;
////                    return ResponseData.success(result);
//                } else {
//                    logger.error(obj.toJSONString());
//                    ResponseData responseData = ResponseData.error();
//                    responseData.setData(null);
//                    return responseData;
//
//                }
//            } else {
//                result.setFlag(1);
//                ResponseData responseData = ResponseData.success();
//                responseData.setData(result);
//                return responseData;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            ResponseData responseData = ResponseData.error();
//            responseData.setData(null);
//            return responseData;
//        }
//
//    }


    /**
     * 处理空中金融新户开户老户绑卡
     *
     * @param vo OpenAccountVo
     * @return ResultEnum
     */
    @Override
    public ResultEnum processAccount(OpenAccountVo vo) {
        try {
            if (vo.getP2pId() != null) { // 已经空中金融开过户
                BindCardRequest bindCardRequest = new BindCardRequest(); // 绑卡请求
                bindCardRequest.setAccountId(vo.getP2pId());
                bindCardRequest.setBankCardNo(vo.getCardNo());
//                bindCardRequest.setChannelCode(kzjrConfig.getChannelCode());
                bindCardRequest.setSmsCode(vo.getSmsCode());
                bindCardRequest.setMobile(vo.getMobile());
                JSONObject result = kzjrService.bindCard(bindCardRequest); // 空中金融绑定银行卡

                if (result.getInteger("status") == 0) { // 正常
                    UserKzjrInfo info = new UserKzjrInfo();
                    info.setAccountId(vo.getP2pId());
                    info.setBankAccount(vo.getCardNo());
                    info.setUserId(vo.getUserId());
                    info.setStatus(0);
//                    userAccountService.saveInfo(info);
                    logger.info(JSON.toJSONString(bindCardRequest) + "bind***" + result.toJSONString());
                    try {
                        userAccountService.updateBankAccByP2pId(vo.getP2pId(), vo.getCardNo()); // 更新t_user_kzjr表信息
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return ResultEnum.CORRECT;
                } else if (result.getInteger("status") == 5015) {
                    logger.error(JSON.toJSONString(bindCardRequest) + "bind***" + result.toJSONString());
                    return ResultEnum.INVALID;
                } else if (result.getInteger("status") == 5002) {
                    logger.error(JSON.toJSONString(bindCardRequest) + "bind***" + result.toJSONString());
                    return ResultEnum.ERROR;
                } else {
                    logger.error(JSON.toJSONString(bindCardRequest) + "bind***" + result.toJSONString());
                    return ResultEnum.UNKNOW;
                }
            } else { // 空中金融新户
                OpenAccountRequest openAccountRequest = new OpenAccountRequest();
                openAccountRequest.setName(vo.getName());
                openAccountRequest.setCardNo(vo.getCardNo());
                openAccountRequest.setSmsCode(vo.getSmsCode());
                openAccountRequest.setMobile(vo.getMobile());
                openAccountRequest.setIdNo(vo.getIdNo());
                openAccountRequest.setIdType(vo.getIdType());
                openAccountRequest.setChannelCode(kzjrConfig.getChannelCode());
                JSONObject result = kzjrService.accountOpen(openAccountRequest); // 开户

                if (result.getInteger("status") == 0) {
                    String accountId = result.getJSONObject("data").getString("accountId");
                    UserKzjrInfo info = new UserKzjrInfo();
                    info.setAccountId(accountId);
                    info.setBankAccount(vo.getCardNo());
                    info.setUserId(vo.getUserId());
                    info.setStatus(0);
                    userAccountService.saveInfo(info); // 保存t_user_kzjr表信息
                    logger.info(JSON.toJSONString(openAccountRequest) + "open***" + result.toJSONString());
                    return ResultEnum.CORRECT;
                } else if (result.getInteger("status") == 5015) {
                    logger.error(JSON.toJSONString(openAccountRequest) + "open***" + result.toJSONString());
                    return ResultEnum.INVALID;
                } else if (result.getInteger("status") == 5002) {
                    logger.error(JSON.toJSONString(openAccountRequest) + "open***" + result.toJSONString());
                    return ResultEnum.ERROR;
                } else {
                    logger.error(JSON.toJSONString(openAccountRequest) + "open***" + result.toJSONString());
                    return ResultEnum.UNKNOW;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEnum.UNKNOW;
        }
    }

    @Override
    public String accountOpenPage(KzjrOpenAccountVo vo) {
        KzjrOpenAcountRequest request = new KzjrOpenAcountRequest();
//        request.setCardNo(vo.getCardNo());
        request.setIdNo(vo.getIdNo());
        request.setIdType(vo.getIdType());
        request.setMobile(vo.getMobile());
        request.setName(vo.getName());
        request.setReturnUrl(vo.getReturnUrl());
        String result = kzjrService.openAccountPage(request);
        logger.info(vo.getName() + ":" + vo.getMobile() + "开户请求结果" + result);
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(null!=this.kzjrConfig){
            logger.info("kzjr conf " + ToStringBuilder.reflectionToString(kzjrConfig));
        }

    }
}
