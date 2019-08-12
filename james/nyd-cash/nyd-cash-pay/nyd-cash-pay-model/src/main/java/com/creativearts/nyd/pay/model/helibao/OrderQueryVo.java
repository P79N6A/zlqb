package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderQueryVo implements Serializable {

    //交易类型
    private String P1_bizType = "QuickPayQuery";

    //商户订单号
    private String P2_orderId;

    //商户编号
    private String P3_customerNumber;

    //签名
    private String sign;
}
