package com.zhiwang.zfm.controller.task;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nyd.order.api.zzl.OrderForZLQServise;
import com.nyd.order.model.OrderInfo;
import com.nyd.zeus.api.payment.PaymentRiskRecordService;
import com.nyd.zeus.model.PaymentRiskRecordVo;

@Component
public class TaskRisk {

	private Logger logger = LoggerFactory.getLogger(TaskRisk.class);
	
	@Autowired
	private PaymentRiskRecordService paymentRiskRecordService;
	
	@Autowired
	private OrderForZLQServise orderForZLQServise;
	
	private static boolean isRunProcess = false;
	private static boolean isRunReady = false;
	
	@Scheduled(cron="${auto_risk_process}")
	public void processData(){
		if(isRunProcess)return;
		isRunProcess = true;
		
		try {
			logger.info("风控调用还款主动还款处理中开始");
			List<PaymentRiskRecordVo> list = paymentRiskRecordService.getProcessData();
			if (list == null || list.size() == 0) {
				logger.info("风控调用还款主动还款处理中结束");
				return;
			}
			for (PaymentRiskRecordVo paymentRiskRecord : list) {
				paymentRiskRecordService.batchQueryRepayTask(paymentRiskRecord);
			}
			logger.info("风控调用还款主动还款处理中结束");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			isRunProcess = false;
		}
	}
	@Scheduled(cron="${auto_risk_ready}")
	public void readyData(){
		if(isRunReady)return;
		isRunReady = true;
		
		try {
			logger.info("风控调用还款主动还款开始");
			List<PaymentRiskRecordVo> list = paymentRiskRecordService.getReadyData();
			if (list == null || list.size() == 0) {
				logger.info("风控调用还款主动还款结束");
				return;
			}
			for (PaymentRiskRecordVo paymentRiskRecord : list) {
				paymentRiskRecordService.batchRepayTask(paymentRiskRecord);
			}
			logger.info("风控调用还款主动还款结束");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			isRunReady = false;
		}
	}
	@Scheduled(cron="${auto_risk_invalid}")
	public void doInvalid(){
		try {
			logger.info("风控调用置为失效开始");
			paymentRiskRecordService.doInvalid();
			logger.info("风控调用置为失效结束");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
		}
	}
	@Scheduled(cron="${auto_notice_invalid}")
	public void doNoticeInvalid(){
		try {
			logger.info("风控调用置为失效开始");
			List<PaymentRiskRecordVo> list =  paymentRiskRecordService.getNotNoticeData();
			if(list == null || list.size()==0) {
				logger.info("风控调用置为失效结束");
				return;
			}
			for (PaymentRiskRecordVo paymentRiskRecord : list) {
				try {
					OrderInfo info = new OrderInfo();
					info.setOrderNo(paymentRiskRecord.getOrderNo());
					info.setMark("2");
					try {
						orderForZLQServise.findOrderStatus(info);
					}catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
					paymentRiskRecord.setNoticeStatus(1);
					paymentRiskRecordService.update(paymentRiskRecord);
					
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
			logger.info("风控调用置为失效结束");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
}
