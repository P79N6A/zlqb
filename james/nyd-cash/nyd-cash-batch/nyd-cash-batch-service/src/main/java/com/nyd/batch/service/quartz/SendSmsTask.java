package com.nyd.batch.service.quartz;

import com.alibaba.fastjson.JSON;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.service.BillBatchService;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.api.OrderDetailContract;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Component("sendSmsTask")
public class SendSmsTask {
    Logger logger  = LoggerFactory.getLogger(SendSmsTask.class);

    @Autowired
    private OrderDetailContract orderDetailContract;

    @Autowired
    private BillBatchService billBatchService;

    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private UserSourceContract userSourceContract;

    public void run() {
        logger.info("发送短信task*****start");
        List<Bill> kdqBillList = billBatchService.getSmsBills();
//        List<SmsRequest> kdqCellPhones = new ArrayList<>();
        if(kdqBillList != null) {
            for (Bill bill:kdqBillList){
                Date promiseDate = bill.getPromiseRepaymentDate();
                if(shouldSendSms(promiseDate)){
                    String orderNo = bill.getOrderNo();
                    OrderDetailInfo orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                    int loopCount=10;
                    while (orderDetailInfo==null&&loopCount>0){
                        orderDetailInfo = orderDetailContract.getOrderDetailByOrderNo(orderNo).getData();
                        loopCount--;
                    }
                    if(orderDetailInfo==null){
                        logger.error(orderNo+"这个订单号没有查出");
                        continue;
                    }

                    SmsRequest request = new SmsRequest();
                    request.setSmsType(9);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(orderDetailInfo.getMobile());
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        request.setAppName(appName);
                    }
                    request.setCellphone(orderDetailInfo.getMobile());
                    Map<String,Object> map = new HashMap<>();
                    map.put("金额",bill.getWaitRepayAmount().doubleValue());
                    map.put("日期",DateFormatUtils.format(promiseDate,"yyyy-MM-dd"));
                    request.setMap(map);
                   ResponseData responseData = sendSmsService.sendSingleSms(request);
                   logger.info("发送短信结果 为"+ JSON.toJSONString(responseData));
                }
            }
        }
        logger.info("发送短信task*****end");
    }

    private boolean shouldSendSms(Date promiseDate) {
        try {
            Date promiseDateNormal = DateUtils.parseDate(DateFormatUtils.format(promiseDate, "yyyy-MM-dd"), "yyyy-MM-dd");
            Date nowDateNormal = DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
            long days = (promiseDateNormal.getTime() - nowDateNormal.getTime()) / (1000 * 3600 * 24);
            logger.info(promiseDateNormal.toString());
            logger.info(nowDateNormal.toString());
            logger.info("天数"+days);
            if (days == 3 || days == 1) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            logger.error("判断时间间隔异常"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
