//package com.nyd.capital.ws.controller;
//
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.nyd.capital.service.kdlc.KdlcCreateOrderService;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
// * 口袋理财资产
// * @author zhangch
// * @date  2018-09-28
// *
// */
//@RestController
//@RequestMapping("/nyd/capital/kdlc")
//public class KdlcCreateOrderController {
//
//	Logger logger=LoggerFactory.getLogger(KdlcCreateOrderController.class);
//
//	@Autowired
//	private KdlcCreateOrderService kdlcCreateOrderService;
//
//	@RequestMapping(value="/createOrder",produces="application/json",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded")
//	public ResponseData createOrder(Map<String,String> paramMap) {
//		try {
////			String userId = request.getParameter("userId");
//			return kdlcCreateOrderService.createOrder(paramMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//}
