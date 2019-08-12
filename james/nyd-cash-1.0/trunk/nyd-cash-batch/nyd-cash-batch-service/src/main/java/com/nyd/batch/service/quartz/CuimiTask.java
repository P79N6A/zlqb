package com.nyd.batch.service.quartz;

import com.nyd.batch.service.CuimiService;
import com.nyd.batch.service.RepayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cong Yuxiang
 * 2018/1/2
 **/
@Component("cuimiTask")
public class CuimiTask {
    Logger logger = LoggerFactory.getLogger(CuimiTask.class);
    @Autowired
    private CuimiService cuimiService;
    @Autowired
    private RepayService repayService;
    public void run(){
        logger.info("催米start");
        try {
            cuimiService.generateCuimiExcel();
        }catch (Exception e1){
            e1.printStackTrace();
        }
        try {
            cuimiService.generateOverdueReturnStatus();
        }catch (Exception e2){
            e2.printStackTrace();
        }
        try {
            repayService.repayBillOnTheDay();
        }catch (Exception e3){
            e3.printStackTrace();
        }
        logger.info("催米end");
    }

    public void run1(){
        logger.info("催米1111start");
        cuimiService.generateCuimiExcelAll();
        logger.info("催米1111end");
    }
}
