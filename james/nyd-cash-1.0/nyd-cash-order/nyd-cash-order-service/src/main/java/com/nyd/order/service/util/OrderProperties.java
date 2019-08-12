package com.nyd.order.service.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Dengw on 2018/1/2
 */
@Component
@Getter
@Data
public class OrderProperties {
    @Value("${order.intervalDays}")
    private String intervalDays;

    @Value("${wwjorder.intervalDays}")
    private String wwjIntervalDays;

    @Value("${order.memberDays}")
    private String memberDays;


    @Value("${order.redirect.url}")
    private String redirectUrl;

    //失败订单发送至万花筒服务开关
    @Value("${order.scope.submit.switch}")
    private String scopeSubmitSwitch;

    //万花筒url
    @Value("${order.scope.submit.url}")
    private String scopeSubmitUrl;


    //银码头资产开关
    @Value("${order.jx.on}")
    private String orderJxOn;


    //风控接口地址
    @Value("${limit.service}")
    private String limitServiceUrl;

    //公共支付服务
    @Value("${common.pay.ip}")
    private String commonPayIp;

    //风控接口地址
    @Value("${common.pay.port}")
    private String commonPayPort;

    //风控接口地址
    @Value("${withhold.callback.url}")
    private String withholdCallbackUrl;
    //客服电话
    @Value("${withhold.customer.phone}")
    private String withholdPhone;
    @Value("${xqjie.withhold.customer.phone}")
    private String xqjieWithholdPhone;
    
    @Value("${wwj.withhold.customer.phone}")
    private String wwjWithholdPhone;

    @Value("${fmh.withhold.customer.phone}")
    private String fmhWithholdPhone;

    @Value("${xxx.withhold.customer.phone}")
    private String xxxWithholdPhone;

 	@Value("${user.card.list.query}")
    private  String cardListQuery ;
}
