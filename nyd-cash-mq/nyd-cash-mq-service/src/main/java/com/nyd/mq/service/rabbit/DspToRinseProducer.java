package com.nyd.mq.service.rabbit;

import com.nyd.dsp.model.msg.DspToRinseMsg;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2017/12/27
 */
@Component
public class DspToRinseProducer {
    @Autowired
    RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(DspToRinseMsg msg){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,msg);
    }

    public String getPattern() {
        return "dsp.nyd";
    }
}
