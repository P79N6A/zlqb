///**
// * Project Name:nyd-cash-pay-ws
// * File Name:ChangJieController.java
// * Package Name:com.creativearts.nyd.web.controller.changjie
// * Date:2018年9月4日下午4:46:47
// * Copyright (c) 2018, wangzhch All Rights Reserved.
// *
//*/
//
//package com.creativearts.nyd.web.controller.changjie;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.creativearts.nyd.pay.model.business.changjie.ZftQuickPayMentVo;
//import com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmRequest;
//import com.creativearts.nyd.pay.service.changjie.ChangJieQuickPayService;
//import com.nyd.pay.entity.ThirdPartyPaymentChannel;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
// * ClassName:畅捷支付对接 <br/>
// * Date:     2018年9月4日 下午4:46:47 <br/>
// * @author   wangzhch
// * @version  
// * @since    JDK 1.8
// * @see 	 
// */
//@RestController
//@RequestMapping(value="/pay/cj")
//public class ChangJieQuickPayController {
//	private Logger log = LoggerFactory.getLogger(ChangJieQuickPayController.class);
//	
//	@Autowired
//	private ChangJieQuickPayService changJieCardService;
//	
//	/**
//	 * 
//	 * paymentSmsConfirm:(支付确认接口). <br/>
//	 * @author wangzhch
//	 * @return
//	 * @since JDK 1.8
//	 */
//	@RequestMapping(value="/paymentSmsConfirm",method = RequestMethod.POST)
//	public ResponseData paymentSmsConfirm(@RequestBody PaymentSmsConfirmRequest paymentSmsConfirmRequest) {
//		log.info("支付确认接口Controller请求参数打印:{}",JSON.toJSONString(paymentSmsConfirmRequest));
//		ResponseData data = changJieCardService.paymentSmsConfirm(paymentSmsConfirmRequest);
//		log.info("支付确认接口Controller,响应结果:{}",JSON.toJSONString(data));
//		return data;
//	}
//	
//	/**
//	 * 
//	 * zftQuickPayment:(直接支付请求接口). <br/>
//	 * @author wangzhch
//	 * @return
//	 * @since JDK 1.8
//	 */
//	@RequestMapping(value="/zftQuickPayment",method = RequestMethod.POST)
//	public ResponseData zftQuickPayment(@RequestBody ZftQuickPayMentVo zftQuickPayMentVo) {
//		try {
//			log.info("直接支付请求接口Controller请求参数打印:{}",JSON.toJSONString(zftQuickPayMentVo));
//			ResponseData data = changJieCardService.zftQuickPayment(zftQuickPayMentVo);
//			log.info("直接支付请求接口Controller,响应结果:{}",JSON.toJSONString(data));
//			return data;
//		} catch (Exception e) {
//			log.error("直接支付请求接口异常打印:{}",e);
//			return ResponseData.error("服务器开小差");
//		}
//		
//	}
//	
//	/**
//	 * 
//	 * queryTrade:(向畅捷发起订单查询接口). <br/>
//	 * @author wangzhch
//	 * @param queryTradeRequest
//	 * @return
//	 * @since JDK 1.8
//	 */
//	@RequestMapping(value="/queryTrade",method = RequestMethod.POST)
//	public ResponseData queryTrade(@RequestBody ThirdPartyPaymentChannel thirdPartyPaymentChannel) {
//		log.info("订单查询请求接口Controller请求参数打印:{}",JSON.toJSONString(thirdPartyPaymentChannel));
//		ResponseData data = changJieCardService.queryTrade(thirdPartyPaymentChannel);
//		log.info("订单查询请求接口Controller,响应结果:{}",JSON.toJSONString(data));
//		return data;
//	}
//}
//
