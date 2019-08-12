/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieBindCardServiceImpl.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.imp
 * Date:2018年9月4日下午6:43:19
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.imp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.creativearts.nyd.pay.model.business.changjie.ZftQuickPayMentVo;
import com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmRequest;
import com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmResponse;
import com.creativearts.nyd.pay.model.changjie.QueryTradeResponse;
import com.creativearts.nyd.pay.model.changjie.ZftQuickPaymentRequest;
import com.creativearts.nyd.pay.model.changjie.ZftQuickPaymentResponse;
import com.creativearts.nyd.pay.model.enums.PayStatus;
import com.creativearts.nyd.pay.model.enums.SourceType;
import com.creativearts.nyd.pay.service.Constants;
import com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService;
import com.creativearts.nyd.pay.service.changjie.enums.ChangJieEnum;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJieConfig;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJiePublicQuickPayRequestConfig;
import com.creativearts.nyd.pay.service.changjie.util.ChangJieSupplyUtil;
import com.creativearts.nyd.pay.service.changjie.util.ChangJiePayUtil;
import com.creativearts.nyd.pay.service.changjie.util.JsonUtil;
import com.creativearts.nyd.pay.service.helibao.util.DingdingUtil;
import com.creativearts.nyd.pay.service.validator.ValidateUtil;
import com.nyd.pay.dao.ThirdPartyPaymentChannelDao;
import com.nyd.pay.entity.ThirdPartyPaymentChannel;
import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.support.model.ResponseData;
import com.tasfe.framework.uid.service.BizCode;
import com.tasfe.framework.uid.service.IdGenerator;

/**
 * ClassName:畅捷支付实现 <br/>
 * Date:     2018年9月4日 下午6:43:19 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Service
public class ChangJieQuickPayServiceImpl implements ChangJieQuickPayService {

	Logger log = LoggerFactory.getLogger(ChangJieQuickPayServiceImpl.class);
	@Autowired
	private ChangJiePublicQuickPayRequestConfig request;
	
	@Autowired
	private ChangJieConfig changJieConfig;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
    private ValidateUtil validateUtil;
	
	@Autowired
	private ThirdPartyPaymentChannelDao orderChannelDao;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	/**
	 * 
	 * 支付确认接口
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService#paymentSmsConfirm(com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmRequest)
	 */
	@Override
	public ResponseData paymentSmsConfirm(PaymentSmsConfirmRequest paymentSmsConfirmRequest) {
		log.info("畅捷确认支付请求参数:",JSON.toJSON(paymentSmsConfirmRequest));
		
		try {
			validateUtil.validate(paymentSmsConfirmRequest);
			//确认支付请求参数封装
			Map<String, String> map = ChangJiePayUtil.paymentSmsConfirmRequestParms(paymentSmsConfirmRequest,request,changJieConfig);
			log.info("支付确认接口参数组装打印:{}",JSON.toJSONString(map));

			//发起确认支付请求
			String result = ChangJieSupplyUtil.gatewayPost(map,request.getInputCharset(),changJieConfig.getZaoYiPrivateKey(),changJieConfig.getChangJieReqUrl());
			log.info("调用畅捷支付确认接口响应结果:{}",JSON.toJSONString(result));

			PaymentSmsConfirmResponse response= JsonUtil.readValueToBean(result,PaymentSmsConfirmResponse.class);
			log.info("确认支付响应的实体类:",JSON.toJSON(response));
			
			
//			String result2 = "{\"AcceptStatus\":\"S\",\"AppRetMsg\":\"交易成功\",\"AppRetcode\":\"QT000000\",\"InputCharset\":\"UTF-8\",\"OrderTrxid\":\"101153811526161921963\",\"PartnerId\":\"200002020066\",\"PayTrxId\":\"301153811526160904090\",\"RetCode\":\"S0001\",\"RetMsg\":\"受理成功\",\"Sign\":\"a4lp2et5Cf/1VvgQ4IMcah1B57AacYYhU+NbFy4ogIyuNjYn/0zCxSHgYiWRtdQFb6lQaScQdDJTwL11fhJ+PxevP7qTHEiRX9EWE5RgGx4dSvPy6saMh7+G9pzKzUknXtTXXgN96rWN9d5dUWD4In9lB1IAx5Iu+f67fsX02tY=\",\"SignType\":\"RSA\",\"Status\":\"S\",\"TradeDate\":\"20180928\",\"TradeTime\":\"141456\",\"TrxId\":\"cj101538115259170001\"}";
//			PaymentSmsConfirmResponse response= JsonUtil.readValueToBean(result2,PaymentSmsConfirmResponse.class);
//			log.info("转换成PaymentSmsConfirmponse:{}",JSON.toJSONString(response)); 
			
			
			if(PayStatus.CJ_FAIL.equals(response.getAcceptStatus())) {
				log.error("调用畅捷[支付确认接口]响应结果失败,订单号transId:{}",paymentSmsConfirmRequest.getTransId());
				//DingdingUtil.getErrMsg("调用畅捷支付确认接口,响应结果失败,失败信息:"+response.getRetMsg()+"订单号:"+paymentSmsConfirmRequest.getTransId());
				return ResponseData.error(response.getRetMsg());
			}
			if(PayStatus.CJ_RUNNING.getCode().equals(response.getStatus())) {
				return ResponseData.success(response);
			}
			ThirdPartyPaymentChannel thirdPayment = orderChannelDao.queryThirdPartyOrderByTransId(response.getTrxId());
			if(null == thirdPayment) {
				log.error("调用畅捷[支付确认接口],订单号transId:{},在DB中不存在",paymentSmsConfirmRequest.getTransId());
				return ResponseData.error("请先获取验证码");
			}

			/**
			 * 此处以确认支付返回的结果去修改了订单状态，
			 * Status为成功，表示扣钱成功
			 * Status为失败，不能简单判断交易订单的失败，请以异步通知结果为准或者通过查询接口确认订单状态(故不改订单状态)
			 * xxd_zeus.t_repay表中以回调为基准
			 */
			//平台流水号
			thirdPayment.setThirdPaymentSerialNo(response.getOrderTrxId());
			//交易订单号
			thirdPayment.setTransId(response.getTrxId());
			thirdPayment.setUpdateTime(new Date());
			if(PayStatus.CJ_FAIL.getCode().equals(response.getStatus())) {
				//交易失败，不修改订单状态
				//thirdPayment.setStatus(2);//支付确认接口同步响应结果为失败时不能简单判断交易订单的失败，请以异步通知结果为准或者通过查询接口确认订单状态
				thirdPayment.setRemarks(response.getRetMsg());
				orderChannelDao.updateThirdPartyOrderStatus(thirdPayment);
				log.info("订单号:"+response.getPayTrxId()+"更新成功");
				return ResponseData.error(response.getRetMsg());
			}
			if(PayStatus.CJ_SUCESS.getCode().equals(response.getStatus())) {
				thirdPayment.setStatus(0);
				//畅捷支付支付系统内部流水号
				thirdPayment.setBankSerialNo(response.getPayTrxId());
				if(StringUtils.isNotBlank(thirdPayment.getRemarks())) {
					thirdPayment.setRemarks("");
				}
			}
			orderChannelDao.updateThirdPartyOrderStatus(thirdPayment);
			log.info("订单号:"+response.getPayTrxId()+"更新成功");
			return ResponseData.success(response);
			
		} catch (Exception e) {
			log.error("支付确认接口异常:{}",e);
			return ResponseData.error("服务器开小差");
		}
	}
	
	/**
	 * 
	 * 直接支付请求接口
	 * @throws Exception 
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService
	 * #zftQuickPayment(com.creativearts.nyd.pay.model.changjie.ZftQuickPaymentRequest)
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public ResponseData zftQuickPayment(ZftQuickPayMentVo zftQuickPayMentVo) throws Exception {
		log.info("直接支付请求接口serviceImp请求参数打印:{}",JSON.toJSONString(zftQuickPayMentVo));
		//参数校验
		validateUtil.validate(zftQuickPayMentVo);
		
		/**畅捷支付暂时只支持 1:充值现金券memFee,2:支付评估费cashCoupon*/
		if (!zftQuickPayMentVo.getSourceType().equals(SourceType.MEMBER_FEE.getType()) && !zftQuickPayMentVo.getSourceType().equals(SourceType.CASH_COUPON.getType())) {
			log.error("畅捷支付暂时只支持充值现金券和支付评估费");
			return ResponseData.error("服务器开小差");
		}
		
		//创建订单
		ThirdPartyPaymentChannel thirdPayment = createOrder(zftQuickPayMentVo);
		orderChannelDao.save(thirdPayment);
		
		//转换成畅捷支付的请求参数(部分请求参数)
		ZftQuickPaymentRequest zftQuickPaymentRequest = busReqToChannelRequestByZftQuickPayment(thirdPayment);
	 	log.info("zftQuickPaymentRequest object:"+JSON.toJSON(zftQuickPaymentRequest));

	 	//转换成请求map集合
		Map<String, String> map = ChangJiePayUtil.zftQuickPaymentRequestParms(zftQuickPaymentRequest,request,changJieConfig);
		log.info("直接支付请求接口参数组装打印:{}",JSON.toJSONString(map));

		//向畅捷发起请求
		String result = ChangJieSupplyUtil.gatewayPost(map,request.getInputCharset(),changJieConfig.getZaoYiPrivateKey(),changJieConfig.getChangJieReqUrl());
		log.info("调用畅捷直接支付请求接口响应结果:{}",JSON.toJSONString(result));

		ZftQuickPaymentResponse response= null;
		if (result != null){
			//将返回结果转换成实体类
			response= JsonUtil.readValueToBean(result,ZftQuickPaymentResponse.class);
			log.info("转换成zftQuickPayment:{}",JSON.toJSONString(response));
		}

		//这里要加if判断https请求不验签    || "P".equals(response.getAcceptStatus())
		if(PayStatus.CJ_FAIL.getCode().equals(response.getAcceptStatus()) || PayStatus.CJ_FAIL.getCode().equals(response.getStatus())) {
			log.info("调用畅捷直接支付请求接口,响应结果状态为F:失败,用户编号:{}",zftQuickPayMentVo.getUserId());
			//DingdingUtil.getErrMsg("调用畅捷直接支付请求接口,响应结果失败,失败信息:"+response.getRetMsg()+"用户编号userId:"+zftQuickPayMentVo.getUserId());	
			thirdPayment.setStatus(1);
			thirdPayment.setRemarks(response.getRetMsg());
			orderChannelDao.updateThirdPartyOrderStatus(thirdPayment);
			return ResponseData.error(response.getRetMsg());
		}
		return ResponseData.success(response);
	}
	
	private ZftQuickPaymentRequest busReqToChannelRequestByZftQuickPayment(ThirdPartyPaymentChannel thirdPayment) {
		ZftQuickPaymentRequest zftQuickPaymentRequest = new ZftQuickPaymentRequest();
		//用户标识
		zftQuickPaymentRequest.setMerUserId(thirdPayment.getUserId());
		//持卡人姓名
		zftQuickPaymentRequest.setCstmrNm(thirdPayment.getRealName());
		//证件号
		zftQuickPaymentRequest.setIdNo(thirdPayment.getCardNo());
		//卡号
		zftQuickPaymentRequest.setBkAcctNo(thirdPayment.getBankNo());
		//银行预留手机号
		zftQuickPaymentRequest.setMobNo(thirdPayment.getPhoneNo());
		//交易金额
		zftQuickPaymentRequest.setTrxAmt(thirdPayment.getActualAmount());
		//商户网站唯一订单号
		zftQuickPaymentRequest.setTrxId(thirdPayment.getTransId());
		//证件类型：01：身份证
		zftQuickPaymentRequest.setIdTp(ChangJieEnum.IDCARD_TYPE_01.getMsg());
		//卡类型(00 –银行贷记卡;01 –银行借记卡;)
		zftQuickPaymentRequest.setBkAcctTp(ChangJieEnum.CARD_TYPE_01.getMsg());
		//商品名称
		zftQuickPaymentRequest.setOrdrName(String.valueOf(thirdPayment.getOrderType()));

		return zftQuickPaymentRequest;
	}
	
	/**
	 * 
	 * 向畅捷发起订单查询接口
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService
	 * #queryTrade(com.creativearts.nyd.pay.model.changjie.QueryTradeRequest)
	 */
	@Override
	public ResponseData queryTrade(ThirdPartyPaymentChannel thirdPartyPaymentChannel) {
		log.info("向畅捷发起订单查询接口serviceImp请求参数打印:{}",JSON.toJSONString(thirdPartyPaymentChannel));
		try {
			Map<String, String> map = ChangJiePayUtil.queryTradeReceiptconfirmRequestParms(thirdPartyPaymentChannel,request,changJieConfig);
			log.info("向畅捷发起订单查询接口参数组装打印:{}",JSON.toJSONString(map));
			String result = ChangJieSupplyUtil.gatewayPost(map,request.getInputCharset(),changJieConfig.getZaoYiPrivateKey(),changJieConfig.getChangJieReqUrl());
			log.info("向畅捷发起订单查询接口响应结果:{}",JSON.toJSONString(result));
			QueryTradeResponse response= JsonUtil.readValueToBean(result,QueryTradeResponse.class);
			log.info("转换成QueryTradeResponse:{}",JSON.toJSONString(response));
			//这里要加if判断
			if(null == response) {
				return ResponseData.error("服务器开小差");
			}
			return ResponseData.success(response);
		} catch (Exception e) {
			log.info("向畅捷发起订单查询接口异常:{}",e);
			return ResponseData.error("服务器开小差");
		}
	}
	
	/**
	 * 
	 * createOrder:(创建订单). <br/>
	 * @author wangzhch
	 * @param zftQuickPayMentVo
	 * @return
	 * @throws Exception
	 * @since JDK 1.8
	 */
	private ThirdPartyPaymentChannel createOrder(ZftQuickPayMentVo zftQuickPayMentVo) throws Exception {
		ThirdPartyPaymentChannel thirdPay = new ThirdPartyPaymentChannel();
		thirdPay.setUserId(zftQuickPayMentVo.getUserId());
		thirdPay.setSourceOrderId(zftQuickPayMentVo.getBillNo());
		/**
		 * 订单号(针对不同的业务逻辑，创建不同类型的订单号，以便区分)
		 */
//		thirdPay.setTransId("cj"+idGenerator.generatorId(BizCode.ORDER_NYD));

		thirdPay.setRealName(zftQuickPayMentVo.getName());
		thirdPay.setCardNo(zftQuickPayMentVo.getIdCard());
		thirdPay.setBankNo(zftQuickPayMentVo.getBankNo());
		thirdPay.setPhoneNo(zftQuickPayMentVo.getMobile());
		//支付渠道
		thirdPay.setChannelCode("CJ");
		//订单状态   0：成功；1：失败； 2：处理中
		thirdPay.setStatus(2);

		/**
		 * 只有支付评估费才会有用到优惠券和现金券
		 */
		if (zftQuickPayMentVo.getSourceType().equals(SourceType.MEMBER_FEE.getType())) {  //支付评估费
			//优惠券id
			if (StringUtils.isNotBlank(zftQuickPayMentVo.getCouponId())) {
				thirdPay.setCouponId(zftQuickPayMentVo.getCouponId());
			}

			//现金券使用金额
			if (zftQuickPayMentVo.getCouponUseFee() != null && zftQuickPayMentVo.getCouponUseFee().compareTo(new BigDecimal(0)) == 1){
				thirdPay.setCouponUseFee(zftQuickPayMentVo.getCouponUseFee());
			}

			//表示为此笔订单为支付评估费
			thirdPay.setOrderType(2);

			//支付评估费交易订单号
			String orderNo = zftQuickPayMentVo.getUserId() + "M" + (System.currentTimeMillis() + "").substring(2, 11);
			thirdPay.setTransId(orderNo);
		}

		/**
		 * 充值现金券，充值现金券id
		 */
		if (zftQuickPayMentVo.getSourceType().equals(SourceType.CASH_COUPON.getType())){   //充值现金券
			//现金券ID
			if (StringUtils.isNotBlank(zftQuickPayMentVo.getCashId())) {
				thirdPay.setCashId(zftQuickPayMentVo.getCashId());
			}

			//表示为此笔订单为充值现金券
			thirdPay.setOrderType(1);

			//充值现金券交易订单号
			String orderNo = zftQuickPayMentVo.getUserId()+"xj"+(System.currentTimeMillis()+"").substring(2,11);
			thirdPay.setTransId(orderNo);
		}

		//支付金额
		thirdPay.setAmount(zftQuickPayMentVo.getAmount());

		//实际支付金额(这个金额应该是在回调的时候封装值的)
		thirdPay.setActualAmount(zftQuickPayMentVo.getAmount());

		//请求时间
		thirdPay.setReqTime(new Date());
		log.info("创建订单对象:"+JSON.toJSON(thirdPay));
		return thirdPay;
	}

	/**
	 * 
	 * 查询处理中的交易
	 * @throws Exception 
	 * @see com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService#queryOrderStatus()
	 */
	@Override
	public List<ThirdPartyPaymentChannel> queryOrderStatus() throws Exception {
		List<ThirdPartyPaymentChannel> list = orderChannelDao.queryOrderStatus();
		return list;
	}
	
}

