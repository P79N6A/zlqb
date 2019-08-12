package com.nyd.mq.service.rabbit;

import com.nyd.capital.model.RemitMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2017/12/18.
 * 放款成功消息发送
 */
@Component
public class RemitToZeusProducer {

    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RemitMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }
    public String getPattern() {
        return "remit.nyd";
    }
}
