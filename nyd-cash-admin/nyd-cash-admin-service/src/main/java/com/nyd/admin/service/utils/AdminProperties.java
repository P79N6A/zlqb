package com.nyd.admin.service.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2018/1/2
 */
@Component
@Getter
public class AdminProperties {
    @Value("${admin.login.timeout}")
    private String loginTimeout;

    @Value("${nyd.send.smg.switch}")
    private String nydSendMsgSwitch;

    @Value("${send.login.sms.switch}")
    private String loginSmsSwitch;
    
    //公共支付服务
    @Value("${common.pay.ip}")
    private String commonPayIp;

    //风控接口地址
    @Value("${common.pay.port}")
    private String commonPayPort;

    @Value("${admin.find.withhold}")
    private String sendUrl;
}
