package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class BindCardMessageVo implements Serializable {

    //交易类型
    private String rt1_bizType = "QuickPayBindCardValidateCode";

    //商户编号
    private String P2_customerNumber;

    //用户id
    private String P3_userId;

    //商户订单号
    private String P4_orderId;

    //时间戳
    private String P5_timestamp;

    //银行卡号
    private String P6_cardNo;

    //手机号码
    private String P7_phone;

    // 签名
    private String sign;

}
