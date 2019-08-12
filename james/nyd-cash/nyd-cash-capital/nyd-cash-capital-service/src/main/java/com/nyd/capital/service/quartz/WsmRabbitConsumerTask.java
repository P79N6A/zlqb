package com.nyd.capital.service.quartz;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Component("rabbitConsumerTask")
public class WsmRabbitConsumerTask {
    @Autowired(required = false)
    private SimpleMessageListenerContainer listenerContainer;
    public void start() {
        listenerContainer.start();
    }
    public void stop(){
        listenerContainer.stop();
    }
}
