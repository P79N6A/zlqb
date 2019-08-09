package com.nyd.mq.service.rabbit;

import com.creativearts.das.query.api.message.AuditResultMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2018/2/24
 */
@Component
public class AuditToOrderYmtProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(AuditResultMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }

    public String getPattern() {
        return "auditYmt.ymt";
    }
}
