package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmPayVo implements Serializable{

    //交易类型
    private String P1_bizType = "QuickPayConfirmPay";

    //商户编号
    private String P2_customerNumber;

    //商户订单号
    private String P3_orderId;

    //时间戳
    private String P4_timestamp;

    //短信验证码
    private String P5_validateCode;

    //支付IP
    private String P6_orderIp  ="127.0.0.1";

    // 签名
    private String sign;
}
