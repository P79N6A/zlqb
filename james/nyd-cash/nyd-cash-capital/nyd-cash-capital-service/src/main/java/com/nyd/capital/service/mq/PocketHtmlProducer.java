package com.nyd.capital.service.mq;

import com.nyd.capital.model.pocket.PocketHtmlMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuqiu
 */
@Component
public class PocketHtmlProducer {

    private static Logger logger = LoggerFactory.getLogger(PocketHtmlProducer.class);

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public String getPattern() {
        return "pocket_html";
    }


    public void sendMsg(PocketHtmlMessage msg) {
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern, msg);
        logger.info("send msg to " + pattern + ", msg is " + msg.toString());
    }

}
