package com.nyd.user.service.mq;

import com.nyd.application.model.mongo.FilePDFInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by hwei on 2018/2/7.
 */
@Component
public class UserToApplicationProducer {
    private static Logger LOGGER = LoggerFactory.getLogger(UserToApplicationProducer.class);
    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(FilePDFInfo msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
        LOGGER.info("send msg to " + pattern + ", msg is " + msg.toString());
    }
    public String getPattern() {
        return "userToApplicationNyd.nyd";
    }

}
