package com.nyd.push.service.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zhujx on 2017/11/20.
 */
@Component("collectionTask")
public class CollectionTask {

    private static Logger LOGGER = LoggerFactory.getLogger(CollectionTask.class);

    /**
     * 每天定时跑批，推送逾期数据
     */
    public void run() {
        System.out.println("Collection跑批开始");
    }
}
