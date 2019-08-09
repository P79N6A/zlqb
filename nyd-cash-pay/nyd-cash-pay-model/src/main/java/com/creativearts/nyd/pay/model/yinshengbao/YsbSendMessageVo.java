package com.creativearts.nyd.pay.model.yinshengbao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class YsbSendMessageVo implements Serializable{

    //商户编号
    private String accountId;

    //用户 ID
    private String customerId;

    //订单号
    private String orderNo;

    //目的
    private String purpose;

    //金额
    private String amount;

    //商品简称
    private String commodityName;

    //业务种类
    private String businessType;

    //后台通知地址
    private String responseUrl;

    //用户授权码
    private String token;

    //数字签名
    private String mac;

    //姓名
    private String name;

    //身份证号
    private String idCardNo;

    //银行卡号
    private String cardNo;

    //手机号
    private String phoneNo;

}
