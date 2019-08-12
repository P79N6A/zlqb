package com.nyd.batch.service.quartz;

import com.nyd.capital.api.service.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2017/12/15
 **/
@Component("kzjrSubbitAssetTask")
public class KzjrSubbitAssetTask {
    Logger logger  = LoggerFactory.getLogger(KzjrSubbitAssetTask.class);
    @Autowired
    private BatchService batchService;
    public void run() {
        logger.info("kzjr subbit asset*****start");
        try {
            batchService.process();
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("kzjr subbit asset*****end");
    }
}
