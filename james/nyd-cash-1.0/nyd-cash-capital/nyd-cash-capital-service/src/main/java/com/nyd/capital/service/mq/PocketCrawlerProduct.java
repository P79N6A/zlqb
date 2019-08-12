package com.nyd.capital.service.mq;

import com.alibaba.fastjson.JSON;
import com.nyd.capital.model.CapitalMessage;
import com.nyd.capital.model.pocket.CrawlerMessage;
import com.tasfe.framework.rabbitmq.RabbitmqProducerProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author liuqiu
 */
@Component
public class PocketCrawlerProduct {

    private static Logger logger = LoggerFactory.getLogger(PocketCrawlerProduct.class);

    @Autowired
    private RabbitmqProducerProxy rabbitmqProducerProxy;

    public void sendMsg(CrawlerMessage message) {
        String pattern = getPattern();
        rabbitmqProducerProxy.convertAndSend(pattern,message);
        logger.info("send msg to " + pattern + ", msg is " + message.toString());
    }

    public String getPattern() {
        return "crawler";
    }

}
