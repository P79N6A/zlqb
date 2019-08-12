package com.nyd.msg.ws.controller;

import com.nyd.msg.model.SmsRequestBatch;
import com.nyd.msg.service.ISendSmsService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author liat.zhang@gmail.com
 * @tel 15801818092
 * @create 2017-12-23 12:50
 */
@RestController
@RequestMapping("/sms")
public class SmsSenderController {

    @Autowired
    private ISendSmsService iSendSmsService;

    @RequestMapping("/send")
    public ResponseData send(@RequestBody SmsRequestBatch smsRequestBatch){

        iSendSmsService.sendBatchSms(smsRequestBatch);
        return ResponseData.success();
    }
}