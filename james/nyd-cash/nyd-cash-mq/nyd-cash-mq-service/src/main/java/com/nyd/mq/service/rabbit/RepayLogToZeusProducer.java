package com.nyd.mq.service.rabbit;

import com.nyd.zeus.model.RepayInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2018/1/9.
 */
@Component
public class RepayLogToZeusProducer {

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RepayInfo message){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,message);
    }
    public String getPattern() {
        return "repayLog.nyd";
    }
}
