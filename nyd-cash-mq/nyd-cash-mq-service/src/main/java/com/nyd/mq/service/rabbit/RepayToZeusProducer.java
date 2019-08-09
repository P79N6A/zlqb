package com.nyd.mq.service.rabbit;

import com.creativearts.nyd.pay.model.RepayMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2017/12/18.
 * 还款成功消息发送
 */
@Component
public class RepayToZeusProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(RepayMessage msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }
    public String getPattern() {
        return "repay.nyd";
    }
}
