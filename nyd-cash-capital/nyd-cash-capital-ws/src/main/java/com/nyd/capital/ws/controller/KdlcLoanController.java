///**
//* @Title: KdlcLoanController.java
//* @Package com.nyd.capital.ws.controller
//* @Description: TODO
//* @author chenjqt
//* @date 2018年9月28日
//* @version V1.0
//*/
//package com.nyd.capital.ws.controller;
//
//import com.nyd.order.model.kdlc.LoanModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nyd.capital.service.kdlc.KdlcLoanService;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
//* @ClassName: KdlcLoanController
//* @Description: 口袋理财放款
//* @author chenjqt
//* @date 2018年9月28日
//*
//*/
//@RestController
//@RequestMapping("/capital/kdlc")
//public class KdlcLoanController {
//	Logger logger = LoggerFactory.getLogger(KdlcLoanController.class);
//
//	@Autowired
//	KdlcLoanService kdlcLoanService;
//
//
//	/**
//	 *
//	* @author chenjqt
//	* @Description: 查询订单
//	* @param @return
//	* @return ResponseData
//	* @throws
//	 */
//	@RequestMapping(value = "/searchOrder", method = RequestMethod.POST)
//	public ResponseData searchOrder(String yurRef) {
//		return null;
//	}
//}
