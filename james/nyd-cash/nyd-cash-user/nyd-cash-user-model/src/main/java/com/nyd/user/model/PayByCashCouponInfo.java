package com.nyd.user.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单纯利用现金券支付对象
 */
@Data
public class PayByCashCouponInfo implements Serializable{

    //用户id
    private String userId;

    //手机号
    private String accountNumber;

    //会员类型
    private String memberType;

    //优惠券id
    private String couponId;

    //现金券id
    private String cashId;

    //现金券使用金额
    private BigDecimal couponUseFee;

    //评估费原本要支付的金额
//    private BigDecimal recommendFee;
    //马甲名称
    private String appName;
}
