///**
// * Project Name:nyd-cash-pay-ws
// * File Name:ChangJieCollectionPayController.java
// * Package Name:com.creativearts.nyd.web.controller.changjie
// * Date:2018年9月10日下午5:31:17
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
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.creativearts.nyd.collectionPay.model.changjie.CardBinRequest;
//import com.creativearts.nyd.pay.service.changjie.ChangJieCollectionPayService;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
// * ClassName:ChangJieCollectionPayController <br/>
// * Date:     2018年9月10日 下午5:31:17 <br/>
// * @author   wangzhch
// * @version  
// * @since    JDK 1.8
// * @see 	 
// */
//@RestController
//@RequestMapping(value="/pay/cj")
//public class ChangJieCollectionPayController {
//	private Logger log = LoggerFactory.getLogger(ChangJieQuickPayController.class);
//	
//	@Autowired
//	private ChangJieCollectionPayService changJieCollectionPayService;
//	/**
//	 * 
//	 * getCardBin:(卡BIN信息查询). <br/>
//	 * @author wangzhch
//	 * @return
//	 * @since JDK 1.8
//	 */
//	@RequestMapping(value="/getCardBin")
//	public ResponseData getCardBin(@RequestBody CardBinRequest cardBinRequest) {
//		log.info("卡BIN信息查询请求接口Controller请求参数打印:{}",JSON.toJSONString(cardBinRequest));
//		ResponseData data = changJieCollectionPayService.getCardBin(cardBinRequest);
//		log.info("卡BIN信息查询请求接口Controller,响应结果:{}",JSON.toJSONString(data));
//		return data;
//	}
//}
//
