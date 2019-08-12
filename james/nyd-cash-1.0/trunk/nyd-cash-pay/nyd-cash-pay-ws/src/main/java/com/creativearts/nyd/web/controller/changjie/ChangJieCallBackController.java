///**
// * Project Name:nyd-cash-pay-ws
// * File Name:ChangJieCallBackController.java
// * Package Name:com.creativearts.nyd.web.controller.changjie
// * Date:2018年9月5日上午11:24:32
// * Copyright (c) 2018, wangzhch All Rights Reserved.
// *
//*/
//
//package com.creativearts.nyd.web.controller.changjie;
//
//import java.io.IOException;
//import java.net.URLDecoder;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.creativearts.nyd.pay.service.changjie.ChangJieCallBackService;
//import com.tasfe.framework.support.model.ResponseData;
//
///**
// * ClassName:畅捷响应异步通知 <br/>
// * Date:     2018年9月5日 上午11:24:32 <br/>
// * @author   wangzhch
// * @version  
// * @since    JDK 1.8
// * @see 	 
// */
//@RestController
//@RequestMapping("/pay/cj")
//public class ChangJieCallBackController {
//	Logger log = LoggerFactory.getLogger(ChangJieCallBackController.class);
//	@Autowired
//	private ChangJieCallBackService changJieCallBackService;
//	
//	/**
//	 * 
//	 * payCallBack:(支付状态异步通知). <br/>
//	 * @author wangzhch
//	 * @param map
//	 * @return
//	 * @throws IOException 
//	 * @since JDK 1.8   /payCallBack
//	 */
//	@RequestMapping(value = "/payCallBack", method = RequestMethod.POST)
//	@ResponseBody
//	public void payCallBack(@RequestBody String payCallBack,HttpServletResponse response) throws IOException {
//		log.info("畅捷支付异步通知响应结果:{}",JSON.toJSONString(payCallBack));
//		try {
//			ResponseData data = changJieCallBackService.checkPaySign(URLDecoder.decode(payCallBack, "UTF-8"));
//			if("0".equals(data.getStatus())) {
//				response.getWriter().write("success");
//			}
//		} catch (IOException e) {
//			log.error("支付状态异步通知响应异常:{}",e);
//			response.getWriter().write("fail");
//		}
//	}
//}
//
