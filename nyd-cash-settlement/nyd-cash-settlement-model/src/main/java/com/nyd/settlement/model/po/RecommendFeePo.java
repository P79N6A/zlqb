package com.nyd.settlement.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 推荐费查询所展示实体类
 */

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendFeePo {
    //订单号
    private String orderNo;

    //产品类型
    private Integer productType;

    //标签
    private Integer testStatus;

    //客户编号
    private String userId;

    //姓名
    private String name;

    //手机号
    private String mobile;

    //推荐费
    private BigDecimal recommendFee;

    //支付渠道
    private String repayChannel;

    //支付日期
    private Date repayTime;

    //支付手续费
    private BigDecimal thirdProcedureFee;

    //已退金额
    private BigDecimal refundAmount;

    //是否已放款
    private Integer orderStatus;

    //是否使用推荐码（待定字段。。。。。。。。。。。。。。。）
    private Integer invitationCodeFlag;//是否已使用推荐码

    //退款手续费
    private BigDecimal refundFee;

    //剩余可退金额
    private BigDecimal resultRefundAmount;


}
