package com.nyd.msg.ws.controller;

import com.nyd.msg.dao.ISysSmsConfigDao;
import com.nyd.msg.entity.SysSmsConfig;
import com.nyd.msg.model.SmsRequest;
import com.nyd.msg.service.ISendSmsService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/25
 **/
@RestController
@RequestMapping("/nyd/test")
public class TestController {

    @Autowired
    private ISysSmsConfigDao sysSmsConfigDao;

    @Autowired
    private ISendSmsService sendSmsService;

    @RequestMapping("/index")
    @ResponseBody
    public ResponseData index(){
        List<SysSmsConfig> configList = sysSmsConfigDao.queryList();
        if (configList == null || configList.isEmpty()) {
            return ResponseData.error("缺少短信平台");

        }else {
            return ResponseData.success();
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public ResponseData test(SmsRequest smsRequest){
        sendSmsService.sendSingleSms(smsRequest);
        List<SysSmsConfig> configList = sysSmsConfigDao.queryList();
        if (configList == null || configList.isEmpty()) {
            return ResponseData.error("缺少短信平台");
        }else {
            return ResponseData.success();
        }
    }
}
