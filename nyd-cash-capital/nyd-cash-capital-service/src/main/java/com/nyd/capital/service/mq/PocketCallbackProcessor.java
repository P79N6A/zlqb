package com.nyd.capital.service.mq;

import com.nyd.capital.model.pocket.PocketCallbackMessage;
import com.nyd.capital.service.pocket.business.impl.PocketCallbackService;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liuqiu
 */
public class PocketCallbackProcessor implements RabbitmqMessageProcesser<PocketCallbackMessage> {

    private static Logger logger = LoggerFactory.getLogger(PocketCallbackProcessor.class);

    @Autowired
    private PocketCallbackService pocketCallbackService;

    @Override
    public void processMessage(PocketCallbackMessage message) {
        logger.info("begin processing callback  mq,and message is:" + message.toString());
        ResponseData responseData = pocketCallbackService.callback(message);
        logger.info("processing callback mq,and result is:" + ToStringBuilder.reflectionToString(responseData));
    }
}
