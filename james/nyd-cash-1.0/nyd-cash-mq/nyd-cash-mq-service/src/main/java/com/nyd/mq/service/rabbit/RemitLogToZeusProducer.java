package com.nyd.mq.service.rabbit;

import com.nyd.zeus.model.RemitInfo;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2018/1/9.
 */
@Component
public class RemitLogToZeusProducer {

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RemitInfo  msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }
    public String getPattern() {
        return "remitLog.nyd";
    }
}
