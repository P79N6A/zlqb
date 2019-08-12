package com.nyd.user.service.mq;

import com.nyd.user.model.mq.RegisterToHitAccountMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 20:54 2018/9/6
 */
@Component
public class HitAccountProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HitAccountProducer.class);

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendAddHitAccountMsg(RegisterToHitAccountMessage message){
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,message);
        LOGGER.info("send msg to " + pattern + ", msg is " + message.toString());
    }

    public String getPattern() {
        return "hitAccountGnh.gnh";
    }
}
