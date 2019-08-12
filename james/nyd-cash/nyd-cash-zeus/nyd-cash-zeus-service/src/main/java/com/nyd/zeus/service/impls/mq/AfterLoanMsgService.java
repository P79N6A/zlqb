package com.nyd.zeus.service.impls.mq;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;



/**
 * @author shaoqing.liu
 * @date 2018/10/14 23:43
 */
@Component
@RabbitListener(queues = "after_loan_message", concurrency = "2")
public class AfterLoanMsgService {

    private static Logger logger = LoggerFactory.getLogger(AfterLoanMsgService.class);



    @RabbitHandler
    public void process(String content) {
    	
    	
    	
    }
}
