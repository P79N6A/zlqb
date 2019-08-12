package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindCardVo implements Serializable {

    //交易类型
    private String rt1_bizType = "QuickPayBindCard";

    //商户编号
    private String P2_customerNumber;

    //用户id
    private String P3_userId;

    //商户订单号
    private String P4_orderId;

    //时间戳
    private String P5_timestamp;

    //姓名
    private String P6_payerName;

    //证件类型
    private String P7_idCardType = "IDCARD";

    //证件号码
    private String P8_idCardNo;

    //银行卡号
    private String P9_cardNo;

    //手机号码
    private String P13_phone;

    //短信验证码
    private String P14_validateCode;

    // 签名
    private String sign;
}
