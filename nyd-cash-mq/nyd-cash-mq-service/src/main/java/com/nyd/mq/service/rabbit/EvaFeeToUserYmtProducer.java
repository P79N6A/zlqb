package com.nyd.mq.service.rabbit;

import com.ibank.pay.model.mq.EvaFeeInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/12/27
 */
@Component
public class EvaFeeToUserYmtProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(EvaFeeInfo msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }

    public String getPattern() {
        return "evaFee.ibank";
    }
}
