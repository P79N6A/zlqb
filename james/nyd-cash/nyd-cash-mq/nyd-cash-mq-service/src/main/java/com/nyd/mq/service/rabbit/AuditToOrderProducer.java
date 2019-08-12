package com.nyd.mq.service.rabbit;

import com.creativearts.das.query.api.message.AuditResultMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/12/27
 */
@Component
public class AuditToOrderProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(AuditResultMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }

    public String getPattern() {
        return "audit.nyd";
    }
}
