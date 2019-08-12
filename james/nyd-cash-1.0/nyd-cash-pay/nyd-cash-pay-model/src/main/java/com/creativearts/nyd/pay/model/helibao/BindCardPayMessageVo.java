package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindCardPayMessageVo implements Serializable {

    //交易类型
    private String P1_bizType = "QuickPayBindPayValidateCode";

    //商户编号
    private String P2_customerNumber;

    //绑卡ID
    private String P3_bindId;

    //用户ID
    private String P4_userId;

    //商户订单号
    private String P5_orderId;

    //时间戳
    private String P6_timestamp;

    //交易币种
    private String P7_currency = "CNY";

    //交易金额
    private String P8_orderAmount;

    //手机号码
    private String P9_phone;

    //签名
    private String sign;

}
