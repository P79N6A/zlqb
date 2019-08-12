package com.nyd.order.service.rabbit;

import com.alibaba.fastjson.JSON;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.nyd.order.dao.OrderDetailDao;
import com.nyd.order.model.OrderDetailInfo;
import com.nyd.user.api.UserSourceContract;
import com.nyd.user.entity.LoginLog;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Dengw on 2017/12/22
 */
@Component
public abstract class BaseProcesser {
    private static Logger LOGGER = LoggerFactory.getLogger(BaseProcesser.class);

    @Autowired(required = false)
    private ISendSmsService sendSmsService;
    @Autowired
    private UserSourceContract userSourceContract;

    @Autowired
    private OrderDetailDao orderDetailDao;

    public void sendMobileSms(int smsType,String orderNo){
        new Thread(() -> {
            LOGGER.info("begin send msg to user, orderNo is " + orderNo);
            SmsRequest sms = new SmsRequest();
            try {
                List<OrderDetailInfo> detailList = orderDetailDao.getObjectsByOrderNo(orderNo);
                if(detailList != null && detailList.size()>0){
                    sms.setSmsType(smsType);
                    ResponseData data = userSourceContract.selectUserSourceByMobile(detailList.get(0).getMobile());
                    String appName = null;
                    if ("0".equals(data.getStatus())){
                        LoginLog loginLog =(LoginLog)data.getData();
                        if (loginLog != null){
                            appName = loginLog.getAppName();
                        }
                    }
                    if (appName != null) {
                        sms.setAppName(appName);
                    }
                    LOGGER.info("开始发送短信告知审核结果,orderNo is"+orderNo);
                    sms.setCellphone(detailList.get(0).getMobile());
                    sendSmsService.sendSingleSms(sms);
                    LOGGER.info("send mobile sms success, smsRequest is" + JSON.toJSONString(sms));
                }
            } catch (Exception e) {
                LOGGER.error("send msg to user error! orderNo = " + orderNo, e);
            }
        }).start();
    }

}
