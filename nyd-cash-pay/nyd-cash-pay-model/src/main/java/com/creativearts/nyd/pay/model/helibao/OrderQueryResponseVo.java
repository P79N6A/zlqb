package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderQueryResponseVo implements Serializable {

    //交易类型
    private String rt1_bizType;

    //返回码
    private String rt2_retCode;

    //返回信息
    private String rt3_retMsg;

    //商户编号
    private String rt4_customerNumber;

    //商户订单号
    private String rt5_orderId;

    //交易金额
    private String rt6_orderAmount;

    //订单创建时间
    private String rt7_createDate;

    //订单完成时间
    private String rt8_completeDate;

    //订单状态
    private String rt9_orderStatus;

    //合利宝交易流水号
    private String rt10_serialNumber;

    //银行编码
    private String rt11_bankId;

    //银行卡类型
    private String rt10_onlineCardType;

    //银行卡后四位
    private String rt12_cardAfterFour;

    //绑定id
    private String rt15_userId;

    //签名
    private String sign;
}
