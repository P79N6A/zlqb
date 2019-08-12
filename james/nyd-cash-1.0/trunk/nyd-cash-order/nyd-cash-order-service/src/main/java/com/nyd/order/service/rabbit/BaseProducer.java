package com.nyd.order.service.rabbit;

import com.nyd.order.model.msg.OrderMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/11/27
 */
@Component
public abstract class BaseProducer {
    private static Logger LOGGER = LoggerFactory.getLogger(BaseProducer.class);

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(OrderMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
        LOGGER.info("send msg to " + pattern + ", msg is " + msg.toString());
    }

    public abstract String getPattern();
}
