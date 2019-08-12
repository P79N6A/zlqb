package com.nyd.zeus.service.rabbit;

import com.nyd.zeus.model.RepayInfo;
import com.nyd.zeus.service.RepayService;
import com.tasfe.framework.rabbitmq.RabbitmqMessageProcesser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhujx on 2018/1/8.
 */
public class RepayLogToZeusMqConsumer implements RabbitmqMessageProcesser<RepayInfo> {

    private static Logger LOGGER = LoggerFactory.getLogger(RepayLogToZeusMqConsumer.class);

    @Autowired
    RepayService repayService;

    @Override
    public void processMessage(RepayInfo message) {

        if(message != null){
            try {
                repayService.save(message);
            } catch (Exception e) {
                LOGGER.error("保存还款流水or扣取会员费流水", message, e);
                e.printStackTrace();
            }
        }

    }
}
