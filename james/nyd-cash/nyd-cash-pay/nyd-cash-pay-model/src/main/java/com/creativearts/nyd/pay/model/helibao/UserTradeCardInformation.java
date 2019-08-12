package com.creativearts.nyd.pay.model.helibao;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTradeCardInformation implements Serializable {

    //姓名
    private String payerName;

    //证件类型
    private String idCardType;

    //证件号
    private String idCardNo;

    //卡号
    private String cardNo;

    //银行编码
    private String bankId;

    //借贷类型
    private String cardType;

    //手机号
    private String phone;

    //绑定状态
    private String bindStatus;

    //绑定号
    private String bindId;
}
