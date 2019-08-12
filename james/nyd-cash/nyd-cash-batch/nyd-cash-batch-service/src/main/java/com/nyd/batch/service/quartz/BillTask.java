package com.nyd.batch.service.quartz;

import com.nyd.batch.service.BillTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/20.
 */
@Component("billTask")
public class BillTask {

    private static Logger LOGGER = LoggerFactory.getLogger(BillTask.class);

    @Autowired
    BillTaskService billTaskService;

    /**
     * 每天定时跑批，检查逾期账单
     */
    public void run() {
        Date date1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LOGGER.info(df.format(date1) + "batch job start");

//        billTaskService.doBillTask();

        Date date2 = new Date();
        LOGGER.info(df.format(date2)  + "batch job end");
        LOGGER.info("batch consuming time:" + (date2.getTime()-date1.getTime()) + "ms");
    }
}
