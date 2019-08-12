package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendValidateCodeVo implements Serializable{

    //交易类型
    private String P1_bizType = "QuickPaySendValidateCode";

    //商户编号
    private String P2_customerNumber;

    //商户订单号
    private String P3_orderId;

    //时间戳
    private String P4_timestamp;

    //手机号码
    private String P5_phone;

    //签名
    private String sign;
}
