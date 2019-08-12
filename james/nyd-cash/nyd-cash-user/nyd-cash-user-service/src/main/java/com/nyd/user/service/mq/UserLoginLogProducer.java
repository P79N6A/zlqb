package com.nyd.user.service.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nyd.user.entity.LoginLog;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;

/**
 * 
 * @author zhangdk
 *
 */
@Component
public class UserLoginLogProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginLogProducer.class);

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendUserLoginLogMsg(LoginLog message){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,message);
        LOGGER.info("send msg to " + pattern + ", msg is " + message.toString());
    }

    public String getPattern() {
        return "nydUserLoginLog.nyd";
    }
}
