package com.nyd.mq.service.rabbit;

import com.ibank.pay.model.mq.RechargeFeeInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/12/27
 */
@Component
public class RechargeFeeToUserYmtProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RechargeFeeInfo msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }

    public String getPattern() {
        return "rechargeFee.ibank";
    }
}
