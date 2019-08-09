package com.nyd.zeus.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.nyd.zeus.model.RemitInfo;
import com.nyd.zeus.service.RemitService;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhujx on 2018/1/8.
 */
public class RemitLogToZeusMqConsumer implements RabbitmqMessageProcesser<RemitInfo> {

    private static Logger LOGGER = LoggerFactory.getLogger(RemitLogToZeusMqConsumer.class);

    @Autowired
    RemitService remitService;

    @Override
    public void processMessage(RemitInfo message) {

        LOGGER.info("收到的放款流水信息"+JSON.toJSONString(message));

        if(message != null){
            try {
                remitService.save(message);
            } catch (Exception e) {
                LOGGER.error("保存放款流水", message, e);
                e.printStackTrace();
            }
        }

    }
}
