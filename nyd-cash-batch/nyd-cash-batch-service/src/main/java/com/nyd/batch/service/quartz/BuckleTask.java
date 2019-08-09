package com.nyd.batch.service.quartz;

import com.nyd.batch.service.BuckleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2017/12/19
 **/
@Component("buckleTask")
public class BuckleTask {

    private Logger logger = LoggerFactory.getLogger(BuckleTask.class);

    @Autowired
    private BuckleService buckleService;

    public void run(String args){

        logger.info("强扣****************"+args);
        int i = Integer.valueOf(args);
        buckleService.processQK(i);
    }
}
