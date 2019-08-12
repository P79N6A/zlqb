package com.nyd.zeus.service.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForZLQServise;
import com.nyd.zeus.model.sms.SendMsgLoanVO;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;

/**
 * Created by hwei on 2018/2/7.
 */
@Component
public class DuringLoanMqConsumer implements RabbitmqMessageProcesser<SendMsgLoanVO>{

	private static Logger logger = LoggerFactory.getLogger(DuringLoanMqConsumer.class);
	
//	@Autowired
//	private ISendSmsService iSendSmsService; 
	
	@Autowired
	private ZeusForZLQServise zeusForZLQServise; 
	 
	@Override
	public void processMessage(SendMsgLoanVO message) {
		logger.info(" mq发贷中短信开始" + JSONObject.toJSONString(message));
		System.out.println("2222");
		zeusForZLQServise.doSendMsg(message);
		//iSendSmsService.sendSingleSms(smsRequest);
		logger.info(" mq发贷中短信结束");
		
	}
	
	
}
