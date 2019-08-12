package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡解绑请求对象
 */
@Data
public class UnBindCardVo implements Serializable {

    //交易类型
    private String P1_bizType = "BankCardUnbind";

    //商户编号
    private String P2_customerNumber;

    //用户id
    private String P3_userId;

    //绑定id
    private String P4_bindId;

    //订单号
    private String P5_orderId;

    //时间戳
    private String P6_timestamp;

    //签名
    private String sign;
}
