package com.nyd.capital.service.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.CapitalMessage;
import com.nyd.capital.service.CapitalService;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;

/**
 * 
 * @author zhangdk
 *
 */
@Component
public class CapitalRiskProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CapitalRiskProducer.class);

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;
    
    public void sendCapitalMsg(CapitalMessage message){
        String pattern = getPattern();
        String s = JSON.toJSONString(message);
        rabbitmqProducerProxy.convertAndSend(pattern,s);
        LOGGER.info("send msg to " + pattern + ", msg is " + s.toString());
    }

    public String getPattern() {
        return "start_finance_risk.nyd";
    }
}
