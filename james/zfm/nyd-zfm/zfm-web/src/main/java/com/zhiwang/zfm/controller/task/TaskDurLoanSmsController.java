//package com.zhiwang.zfm.controller.task;
//
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSONObject;
//import com.nyd.order.api.zzl.OrderForZLQServise;
//import com.nyd.order.model.OrderInfo;
//import com.nyd.zeus.api.payment.PaymentRiskRecordService;
//import com.nyd.zeus.api.zzl.ZeusForZLQServise;
//import com.nyd.zeus.model.PaymentRiskRecordVo;
//import com.nyd.zeus.model.common.CommonResponse;
//
//@Component
//public class TaskDurLoanSmsController {
//
//	private Logger logger = LoggerFactory.getLogger(TaskDurLoanSmsController.class);
//	
//
//	
//	@Autowired
//	private ZeusForZLQServise zeusForZLQServise;
//	
//
//	@Scheduled(cron="${auto_sms_duringLoan}")
//	public void processData(){
//		logger.info(" 《《《《查询客户回复短信接口开始： ");
//		try {
//			zeusForZLQServise.doReceiveMsg();	
//		} catch (Exception e) {
//			logger.error(e.getMessage(),e);
//			logger.info("查询客户回复短信接口异常");
//		}
//		logger.info("查询客户回复短信接口结束》》》》》 ");
//	}
//
//
//}
