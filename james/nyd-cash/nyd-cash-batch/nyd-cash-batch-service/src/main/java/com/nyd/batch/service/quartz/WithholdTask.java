package com.nyd.batch.service.quartz;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.service.BillTaskService;
import com.nyd.batch.service.util.RestTemplateApi;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component("withholdTask")
public class WithholdTask {

    private static Logger LOGGER = LoggerFactory.getLogger(WithholdTask.class);

//    @Autowired
//    BillTaskService billTaskService;

    @Autowired
    RestTemplateApi restTemplateApi;

    /**
     * 每天定时跑批，检查逾期账单
     */
    public void run() {
        Date date1 = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LOGGER.info(df.format(date1) + "batch job start");

//        billTaskService.doBillTask();
        //此处调用远程的接口
//        getWithholdTask("xxd");
        getWithholdTaskByTimeHalfHour();

        Date date2 = new Date();
        LOGGER.info(df.format(date2)  + "batch job end");
        LOGGER.info("batch consuming time:" + (date2.getTime()-date1.getTime()) + "ms");
    }

    private ResponseData getWithholdTask(String appName) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (appName == null) {
            param.put("appCode", "xxd");
        } else {
            param.put("appCode", appName);
        }
        LOGGER.info("定时代扣跑批： " + JSON.toJSONString(param));
        try {
            ResponseData resp = restTemplateApi.postForObject("http://127.0.0.1:8086/common/pay/schedule/WitholdPay", param, ResponseData.class);
            LOGGER.info("定时代扣跑批：{} ", JSON.toJSONString(resp));
            return resp;
        } catch (Exception e) {
            LOGGER.error("定时代扣跑批失败：{}", e.getMessage());
            return ResponseData.error();
        }
    }

    //半小时跑批一次
    private ResponseData getWithholdTaskByTimeHalfHour() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appCode", "xxd");
        LOGGER.info("半小时跑批一次： " + JSON.toJSONString(param));
        try {
            ResponseData resp = restTemplateApi.postForObject("http://127.0.0.1:8086/common/pay/schedule/WitholdPayByTimeByHalfHour", param, ResponseData.class);
            LOGGER.info("半小时跑批一次：{} ", JSON.toJSONString(resp));
            return resp;
        } catch (Exception e) {
            LOGGER.error("半小时跑批一次失败：{}", e.getMessage());
            return ResponseData.error();
        }
    }

    public static void main(String[] args) {
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime  =  format.format(new Date());
            System.out.println("now:  "+startTime);
            Date date = null;
            date = format.parse(startTime);
            date.setTime(date.getTime() - 30*60*1000);
            System.out.println("半小时前："+format.format(date));
            String endTime  = format.format(date);
            param.put("startTime", startTime);
            param.put("endTime", endTime);
            LOGGER.info("定时代扣跑批： " + JSON.toJSONString(param));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
