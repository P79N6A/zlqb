package com.creativearts.nyd.pay.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/4/23
 **/
@Data
public class PayCarryVo {
    private String memberType;
    private String userId;
    private String mobile;
    private String productCode;

    //优惠券id
    private String couponId;

    //现金券id
    private String cashId;


    //现金券使用金额
    private BigDecimal couponUseFee;

    private String appName;
}
