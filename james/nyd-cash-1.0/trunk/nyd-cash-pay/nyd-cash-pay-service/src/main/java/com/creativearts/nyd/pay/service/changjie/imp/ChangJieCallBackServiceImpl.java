/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieCallBackServiceImpl.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.imp
 * Date:2018年9月12日下午7:44:52
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.imp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.nyd.user.api.UserAccountContract;
import com.nyd.user.model.dto.AccountDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.MemberCarryResp;
import com.creativearts.nyd.pay.model.RepayType;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.RedisProcessService;
import com.creativearts.nyd.pay.service.changjie.ChangJieCallBackService;
import com.creativearts.nyd.pay.service.changjie.enums.ChangJieEnum;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJieConfig;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJiePublicQuickPayRequestConfig;
import com.creativearts.nyd.pay.service.changjie.util.ChangJieSupplyUtil;
import com.creativearts.nyd.pay.service.changjie.util.JsonUtil;
import com.creativearts.nyd.pay.service.changjie.util.RsaUtil;
import com.nyd.member.model.msg.MemberFeeLogMessage;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.pay.dao.ThirdPartyPaymentChannelDao;
import com.nyd.pay.entity.ThirdPartyPaymentChannel;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.nyd.user.model.mq.RechargeFeeInfo;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import com.tasfe.framework.support.model.ResponseData;

/**
 * ClassName:ChangJieCallBackServiceImpl <br/>
 * Date:     2018年9月12日 下午7:44:52 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Service
public class ChangJieCallBackServiceImpl implements ChangJieCallBackService {

	private Logger log = LoggerFactory.getLogger(ChangJieCallBackServiceImpl.class);
	
	@Autowired
	private ChangJiePublicQuickPayRequestConfig request;
	
	@Autowired
	private ChangJieConfig changJieConfig;
	
	@Autowired
	private ThirdPartyPaymentChannelDao thirdPartyPaymentChannelDao;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
    private RedisProcessService redisProcessService;
	
	@Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
	
	@Autowired
    private UserSourceContract userSourceContract;
	
	@Autowired
    private ISendSmsService sendSmsService;

	@Autowired
    private UserAccountContract userAccountContract;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 
	 * 支付异步通知验签
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieCallBackService#checkPaySign(java.lang.String)
	 */
	@Override
	public ResponseData checkPaySign(String payCallBack) {

		try {
			if(StringUtils.isEmpty(payCallBack)) {
				log.info("畅捷支付异步通知参数为空");
				return ResponseData.error("畅捷支付异步通知异常");
			}

			//异步通知结果转换成map集合
			Map<String,String> resMap = JsonUtil.keyValueToMap(payCallBack);
			log.info("正在处理订单:{}",resMap.get("outer_trade_no"));
			log.info("畅捷支付异步通知结果转换成map集合:"+JSON.toJSON(resMap));

			//验签
			String singText = ChangJieSupplyUtil.createLinkString(resMap, false);
			boolean flag = RsaUtil.verify(singText, resMap.get("sign"), changJieConfig.getChangJiePublicKey(),request.getInputCharset());
			//验签失败
			if(flag == false) {
				log.error("畅捷支付异步通知验签失败,订单号:{}",resMap.get("outer_trade_no"));
				return ResponseData.error("验签失败");
			}
			
			// 避免重复的通知
			try {
	            if (redisTemplate.hasKey(Constants.CJ_REDIS_PREFIX + resMap.get("outer_trade_no"))) {
	                log.error("畅捷支付订单:" +resMap.get("outer_trade_no")+"有重复通知");
	                return ResponseData.success();
	            } else {
	                redisTemplate.opsForValue().set(Constants.CJ_REDIS_PREFIX + resMap.get("outer_trade_no"), "1", 2880, TimeUnit.MINUTES);
	            }
	        }catch (Exception e){
	            log.error("写redis出错"+e.getMessage());
	            ResponseData.error("服务器开小差");
	        }
						
			if(ChangJieEnum.TRADE_FINISHED.getMsg().equals(resMap.get("trade_status"))) {
				log.info("交易已结束,清结算已完成,订单号:{}",resMap.get("outer_trade_no"));
				return ResponseData.success();
			}

			//去xxd_user.t_order_channel表中去寻找交易订单号
			ThirdPartyPaymentChannel thirdPay = thirdPartyPaymentChannelDao.queryThirdPartyOrderByTransId(resMap.get("outer_trade_no"));
			if (null == thirdPay) {
				log.error("查询不到订单信息,订单号:{}",resMap.get("outer_trade_no"));
				return ResponseData.error("此笔订单不存在");
			}

			if(thirdPay.getActualAmount().compareTo(new BigDecimal(resMap.get("trade_amount"))) != 0) {
				log.error("交易金额不匹配,订单号:{}",resMap.get("outer_trade_no"));
				return ResponseData.error("交易金额不匹配");
			}
			
			thirdPay.setResTime(sdf.parse(resMap.get("notify_time")));

			//交易金额
            thirdPay.setActualAmount(new BigDecimal(resMap.get("trade_amount")));

            //表示此笔交易订单成功
			if(ChangJieEnum.TRADE_SUCCESS.getMsg().equals(resMap.get("trade_status"))) {
				thirdPay.setStatus(0);//0是支付成功
				if(StringUtils.isNotBlank(thirdPay.getRemarks())) {
					thirdPay.setRemarks("");
				}
                //充值现金券
	            if(1 == thirdPay.getOrderType()) {
	            	RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                    rechargeFeeInfo.setUserId(thirdPay.getUserId());
                    //手机号
                    String mobile = getMobile(thirdPay.getUserId());
                    rechargeFeeInfo.setAccountNumber(mobile);
                    //交易金额
//                    rechargeFeeInfo.setAmount(thirdPay.getActualAmount());
                    rechargeFeeInfo.setAmount(new BigDecimal(resMap.get("trade_amount")));
                    rechargeFeeInfo.setCashId(thirdPay.getCashId());
                    rechargeFeeInfo.setOperStatus(1);          //0：失败  1：成功
                    rechargeFeeInfo.setRechargeFlowNo(resMap.get("inner_trade_no"));
                    rechargeFeeInfo.setStatusMsg("成功");
                    rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                    rechargeFeeInfo.setCashDescription("用户充值收入");
                    log.info("畅捷支付完成，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                        log.info("畅捷支付完成,发送mq现金券对象完毕");
                    }catch (Exception e){
                        log.error("畅捷支付完成,发送mq现金券对象异常",e);
                    }
	            	log.info("充值现金券成功,支付流水对象："+JSON.toJSON(rechargeFeeInfo));

                    MemberCarryResp resp = new MemberCarryResp();
                    resp.setMobile(thirdPay.getPhoneNo());
                    resp.setProductCode("现金券");
                    resp.setStatus("0");
                    redisProcessService.putPayStatusMember(thirdPay.getUserId(),JSON.toJSONString(resp));

                    /**
                     * 充值现金券的流水写入表t_repay
                     */
                    RepayInfo repayInfo = new RepayInfo();
                    repayInfo.setRepayStatus("0");      //0：成功；1：失败
                    //充值现金券的交易订单号(我方请求时传过去的)
                    repayInfo.setBillNo(resMap.get("outer_trade_no"));
                    //支付金额
                    repayInfo.setRepayAmount(new BigDecimal(resMap.get("trade_amount")));
                    //支付渠道
                    repayInfo.setRepayChannel("cj");
                    //交易时间(通知时间)
                    repayInfo.setRepayTime(new Date());
                    //充值现金券的交易流水号(支付平台交易订单号)
                    repayInfo.setRepayNo(resMap.get("inner_trade_no"));
                    repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                    repayInfo.setUserId(thirdPay.getUserId());
                    log.info("充值现金券成功，支付流水对象："+JSON.toJSON(repayInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        log.info("畅捷充值现金券完成,发送充值流水mq对象完毕");
                    }catch (Exception e){
                        log.error("畅捷充值现金券完成,发送充值流水mq对象异常",e);
                    }

	            } else if(2 == thirdPay.getOrderType()) {            //支付评估费
                    MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                    //支付渠道
                    memberModel.setDebitChannel("cj");
                    String mobile = getMobile(thirdPay.getUserId());
                    memberModel.setMobile(mobile);
                    memberModel.setUserId(thirdPay.getUserId());
                    memberModel.setMemberType("1");
                    memberModel.setDebitFlag("1");
                    //优惠券id
                    if (StringUtils.isNotBlank(thirdPay.getCouponId())){
                        memberModel.setCouponId(thirdPay.getCouponId());
                    }
                    //现金券使用金额
                    memberModel.setCouponUseFee(thirdPay.getCouponUseFee());

                    //此笔支付，需要支付的金额
                    memberModel.setPayCash(thirdPay.getActualAmount());
                    log.info("畅捷支付会员费，给member发送mq消息的对象:"+JSON.toJSON(memberModel));

                    try {
                        rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                    }catch (Exception e){
                        log.error("畅捷支付评估费,发送mq异常",e);
                    }

                    MemberCarryResp resp = new MemberCarryResp();
                    resp.setMobile(thirdPay.getPhoneNo());
                    resp.setProductCode("评估费");
                    resp.setStatus("0");
                    redisProcessService.putPayStatusMember(thirdPay.getUserId(),JSON.toJSONString(resp));

                    SmsRequest smsRequest = new SmsRequest();
                    smsRequest.setCellphone(mobile);
                    smsRequest.setSmsType(8);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(mobile);
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        smsRequest.setAppName(appName);
                    }
                    try {
                        ResponseData responseData = sendSmsService.sendSingleSms(smsRequest);
                        log.info("会员费发送短信结果:"+JSON.toJSONString(responseData));
                    }catch (Exception e){
                        log.error("会员费发送短信失败:"+e.getMessage());
                    }

                    //畅捷支付评估费流水
                    RepayInfo repayInfo = new RepayInfo();
                    repayInfo.setRepayStatus("0");
                    //充值现金券的交易订单号(我方请求时传过去的)
                    repayInfo.setBillNo(resMap.get("outer_trade_no"));
                    //支付金额
                    repayInfo.setRepayAmount(new BigDecimal(resMap.get("trade_amount")));
                    //支付渠道
                    repayInfo.setRepayChannel("cj");
                    //交易时间(通知时间)
                    repayInfo.setRepayTime(new Date());
                    //充值现金券的交易流水号(支付平台交易订单号)
                    repayInfo.setRepayNo(resMap.get("inner_trade_no"));
                    repayInfo.setRepayType(RepayType.MFEE.getCode());
                    repayInfo.setUserId(thirdPay.getUserId());
                    //优惠券id
                    if (StringUtils.isNotBlank(thirdPay.getCouponId())){
                        repayInfo.setCouponId(thirdPay.getCouponId());
                    }
                    //现金券使用金额
                    repayInfo.setCouponUseFee(thirdPay.getCouponUseFee());
                    log.info("畅捷支付评估费，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        log.info("畅捷支付评估费,给repay发送mq消息成功");
                    }catch (Exception e){
                        log.error("畅捷支付评估费,给repay发送mq消息异常",e);
                    }

	            }
			}

			//支付失败
			if(ChangJieEnum.TRADE_CLOSED.getMsg().equals(resMap.get("trade_status"))) {
				thirdPay.setStatus(1);
				thirdPay.setRemarks(resMap.get("trade_status"));
				
				if(1 == thirdPay.getOrderType()) {                   //充值现金券失败
	            	RechargeFeeInfo rechargeFeeInfo = new RechargeFeeInfo();
                    rechargeFeeInfo.setUserId(thirdPay.getUserId());
                    String mobile = getMobile(thirdPay.getUserId());
                    rechargeFeeInfo.setAccountNumber(mobile);
                    //交易金额
                    rechargeFeeInfo.setAmount(new BigDecimal(resMap.get("trade_amount")));
                    rechargeFeeInfo.setCashId(thirdPay.getCashId());
                    rechargeFeeInfo.setOperStatus(1);          //0：失败  1：成功
                    rechargeFeeInfo.setRechargeFlowNo(resMap.get("inner_trade_no"));
                    rechargeFeeInfo.setStatusMsg("支付失败");
                    rechargeFeeInfo.setUserType(1);   //用户充值现金券收入
                    rechargeFeeInfo.setCashDescription("用户充值收入");
                    log.info("畅捷支付失败[Fail]，发送mq现金券对象："+JSON.toJSON(rechargeFeeInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rechargeCoupon.nyd", rechargeFeeInfo);
                        log.info("畅捷支付[Fail]，发送mq现金券对象完毕");
                    }catch (Exception e){
                        log.error("畅捷支付[Fail]，发送mq现金券对象异常",e);
                    }
	            	log.info("充值现金券失败[Fail]，支付流水对象："+JSON.toJSON(rechargeFeeInfo));

	            	MemberCarryResp resp = new MemberCarryResp();
                    resp.setMobile(thirdPay.getPhoneNo());
                    resp.setProductCode("现金券");
                    resp.setStatus("1");
                    redisProcessService.putPayStatusMember(thirdPay.getUserId(),JSON.toJSONString(resp));
                    
                    /**
                     * 购买现金券失败，不发送短信
                     * 购买现金券的流水写入表t_repay
                     */
                    RepayInfo repayInfo = new RepayInfo();
                    repayInfo.setRepayStatus("1");      //0：成功；1：失败
                    //充值现金券的交易订单号(我方请求时传过去的)
                    repayInfo.setBillNo(resMap.get("outer_trade_no"));
                    //支付金额
                    repayInfo.setRepayAmount(new BigDecimal(resMap.get("trade_amount")));
                    //支付渠道
                    repayInfo.setRepayChannel("cj");
                    //交易时间(通知时间)
                    repayInfo.setRepayTime(new Date());
                    //充值现金券的交易流水号(支付平台交易订单号)
                    repayInfo.setRepayNo(resMap.get("inner_trade_no"));
                    repayInfo.setRepayType(RepayType.COUPON_FEE.getCode());
                    repayInfo.setUserId(thirdPay.getUserId());
                    log.info("畅捷充值现金券失败，支付流水对象："+JSON.toJSON(repayInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        log.info("畅捷充值现金券失败,发送mq成功");
                    }catch (Exception e){
                        log.error("畅捷充值现金券失败,发送mq异常",e);
                    }

	            }
	            if(2 == thirdPay.getOrderType()) {                 //支付评估费失败
                    MemberFeeLogMessage memberModel = new MemberFeeLogMessage();
                    memberModel.setUserId(thirdPay.getUserId());
                    memberModel.setDebitChannel("cj");
                    String mobile = getMobile(thirdPay.getUserId());
                    memberModel.setMobile(mobile);
                    memberModel.setMemberType("1");
                    memberModel.setDebitFlag("0"); //失败
                    //优惠券id
                    if (StringUtils.isNotBlank(thirdPay.getCouponId())){
                        memberModel.setCouponId(thirdPay.getCouponId());
                    }

                    //现金券使用金额
                    memberModel.setCouponUseFee(thirdPay.getCouponUseFee());

                    //此笔支付，需要支付的金额
                    memberModel.setPayCash(thirdPay.getActualAmount());
                    log.info("畅捷支付会员费失败，给member发送mq消息的对象："+JSON.toJSON(memberModel));

                    try {
                        rabbitmqProducerProxy.convertAndSend("mfee.nyd", memberModel);
                        log.error("畅捷支付评估费失败,发送mq成功");
                    }catch (Exception e){
                        log.error("畅捷支付评估费失败,发送mq异常",e);
                    }

                    MemberCarryResp resp = new MemberCarryResp();
                    resp.setMobile(thirdPay.getPhoneNo());
                    resp.setProductCode("评估费");
                    resp.setStatus("1");
                    redisProcessService.putPayStatusMember(thirdPay.getUserId(),JSON.toJSONString(resp));
                    
                    RepayInfo repayInfo = new RepayInfo();
                    repayInfo.setRepayStatus("1");
                    //充值现金券的交易订单号(我方请求时传过去的)
                    repayInfo.setBillNo(resMap.get("outer_trade_no"));
                    //支付金额
                    repayInfo.setRepayAmount(new BigDecimal(resMap.get("trade_amount")));
                    //支付渠道
                    repayInfo.setRepayChannel("cj");
                    //交易时间(通知时间)
                    repayInfo.setRepayTime(new Date());
                    //充值现金券的交易流水号(支付平台交易订单号)
                    repayInfo.setRepayNo(resMap.get("inner_trade_no"));
                    //交易类型
                    repayInfo.setRepayType(RepayType.MFEE_FAIL.getCode());
                    //userID
                    repayInfo.setUserId(thirdPay.getUserId());
                    //优惠券id
                    if (StringUtils.isNotBlank(thirdPay.getCouponId())){
                        repayInfo.setCouponId(thirdPay.getCouponId());
                    }
                    //现金券使用金额
                    repayInfo.setCouponUseFee(thirdPay.getCouponUseFee());
                    log.info("畅捷支付会员费失败[Fail]，给repay发送mq消息的对象："+JSON.toJSON(repayInfo));

                    try {
                        rabbitmqProducerProxy.convertAndSend("rePayLog.nyd", repayInfo);
                        log.info("畅捷支付评估费失败,给repay发送mq消息成功");
                    }catch (Exception e){
                        log.error("畅捷支付评估费失败,给repay发送mq消息异常",e);
                    }

                    
	            }
			}
			thirdPartyPaymentChannelDao.updateThirdPartyOrderStatus(thirdPay);
			log.info("订单信息状态修改成功,订单号:{}",resMap.get("outer_trade_no"));
			
			Map<String, String> map = new HashMap<String,String>();
			map.put("productCode", thirdPay.getTransId());
			map.put("mobile", thirdPay.getPhoneNo());
			map.put("status", thirdPay.getStatus().toString());
			//将修改的订单存入到redis里面，用来轮询查询他的状态
			redisTemplate.opsForValue().set(Constants.PAY_STATUS + thirdPay.getUserId(), JSON.toJSONString(map),1,TimeUnit.MINUTES);
			return ResponseData.success();

		} catch (Exception e) {
			log.error("服务器开小差了,{}",e);
			return ResponseData.error("服务器开小差了");
		}
	}

    /**
     * 根据userId找到账户号
     * @param userId
     * @return
     */
    private String getMobile(String userId){
        String mobile = "";
        try{
            ResponseData<AccountDto> data = userAccountContract.getAccount(userId);
            if ("0".equals(data.getStatus())){
                AccountDto accountDto = data.getData();
                log.info("find by userId:"+JSON.toJSONString(accountDto));
                mobile = accountDto.getAccountNumber();
                log.info("userId:"+userId+",对应的手机号:"+mobile);
            }
        }catch(Exception e){
            log.error("根据userId查找手机号出错",e);
        }
        return  mobile;

    }


}

