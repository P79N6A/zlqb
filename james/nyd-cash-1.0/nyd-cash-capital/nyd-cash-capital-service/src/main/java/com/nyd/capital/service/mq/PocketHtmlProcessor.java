package com.nyd.capital.service.mq;

import com.nyd.capital.model.pocket.PocketHtmlMessage;
import com.nyd.capital.service.pocket.business.impl.PocketHtmlService;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuqiu
 */
public class PocketHtmlProcessor implements RabbitmqMessageProcesser<PocketHtmlMessage> {

    private static Logger logger = LoggerFactory.getLogger(PocketHtmlProcessor.class);

    @Autowired
    private PocketHtmlService pocketHtmlService;

    @Override
    public void processMessage(PocketHtmlMessage message) {
        logger.info("begin processing order confirmation mq,and message is:" + message.toString());
        ResponseData responseData = pocketHtmlService.confirmOrderHtml(message);
        logger.info("processing order confirmation mq,and result is:" + ToStringBuilder.reflectionToString(responseData));
    }
}
