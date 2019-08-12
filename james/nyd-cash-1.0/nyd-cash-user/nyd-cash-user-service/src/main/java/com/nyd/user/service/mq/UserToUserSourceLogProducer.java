package com.nyd.user.service.mq;

import com.nyd.user.model.mq.UserSourceLogMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserToUserSourceLogProducer {
	private static Logger LOGGER = LoggerFactory.getLogger(UserToUserSourceLogProducer.class);
	
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(UserSourceLogMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
        LOGGER.info("send msg to " + pattern + ", msg is " + msg.toString());
    }
    public String getPattern() {
        return "userToUserSourceLogGnh.gnh";
    }
    
}
