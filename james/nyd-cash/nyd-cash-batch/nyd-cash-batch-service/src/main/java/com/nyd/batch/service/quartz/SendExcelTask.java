package com.nyd.batch.service.quartz;

import com.nyd.batch.service.mail.CuimiMail;
import com.nyd.batch.service.mail.ExpireUserMail;
import com.nyd.batch.service.mail.OverdueStatusMail;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/1/3
 **/
@Component("sendExcelTask")
public class SendExcelTask {
    Logger logger = LoggerFactory.getLogger(SendExcelTask.class);
    @Autowired
    private CuimiMail cuimiMail;
    @Autowired
    private OverdueStatusMail overdueStatusMail;
    @Autowired
    private ExpireUserMail expireUserMail;
    public void run(){
        logger.info("发送excel start");
        try {
            cuimiMail.sendMail();
        }catch (Exception e1){
            e1.printStackTrace();
        }
        try {
            overdueStatusMail.sendMail("/data/cuimi/逾期还款状态" + DateFormatUtils.format(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd") + ".xlsx");
        }catch (Exception e2){
            e2.printStackTrace();
        }
        try {
            expireUserMail.sendMail("/data/cuimi/当天要还款账单" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ".xlsx");
        }catch (Exception e3){
            e3.printStackTrace();
        }
        logger.info("发送excel end");

    }

    public void runManual(String date){
        logger.info("发送excel start");

//        cuimiMail.sendMail("/data/cuimi/侬要贷催收单"+date+".xlsx");
        logger.info("发送excel end");

    }
}
