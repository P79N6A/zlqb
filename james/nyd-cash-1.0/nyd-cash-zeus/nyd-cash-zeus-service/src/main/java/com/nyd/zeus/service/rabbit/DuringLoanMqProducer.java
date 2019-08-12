package com.nyd.zeus.service.rabbit;

import com.creativearts.nyd.pay.model.RepayMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by hwei on 2018/2/7.
 */
@Component
public class DuringLoanMqProducer {
    private static Logger LOGGER = LoggerFactory.getLogger(DuringLoanMqProducer.class);

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RepayMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
        LOGGER.info("send msg to " + pattern + ", msg is " + msg.toString());
    }
//during
    public String getPattern() {
        return "during.sendMsgLoan";
    }
}
