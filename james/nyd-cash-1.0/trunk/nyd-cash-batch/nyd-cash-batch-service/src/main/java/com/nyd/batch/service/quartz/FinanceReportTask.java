package com.nyd.batch.service.quartz;

import com.nyd.batch.service.FinanceReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 财务报表
 * Cong Yuxiang
 * 2017/12/25
 **/
@Component("financeReportTask")
public class FinanceReportTask {
    Logger logger  = LoggerFactory.getLogger(FinanceReportTask.class);
    @Autowired
    private FinanceReportService financeReportService;
    public void run() {
        logger.info("financeReportTask*****start");
        financeReportService.generateReport();
        logger.info("financeReportTask*****end");
    }

    public void test(String date,String remitflag,String repayflag){
        logger.info("离线financeReportTask*****start");
        financeReportService.generateReportDate(date,remitflag,repayflag);
        logger.info("离线financeReportTask*****end");
    }
}
