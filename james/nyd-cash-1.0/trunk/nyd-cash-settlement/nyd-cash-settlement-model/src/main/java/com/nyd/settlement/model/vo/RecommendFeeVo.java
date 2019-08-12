package com.nyd.settlement.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecommendFeeVo {
    private String orderNo;//订单号
    private String userId;//客户编号
    private String productType;//产品类型
    private String label;//标签
    private String name;//姓名
    private String mobile;//手机号
    private String recommendFee;//推荐费
    private String repayChannel;//支付渠道
    private String repayTime;//支付日期
    private BigDecimal thirdProcedureFee = new BigDecimal(0); //支付手续费
    private BigDecimal refundAmount = new BigDecimal(0); //已退金额
    private String orderStatus;//是否放款

    //是否使用推荐码（这个字段待定）
    private String invitationCodeFlag;//是否已使用推荐码

    //退款手续费
    private BigDecimal refundFee;

    //剩余可退金额
    private BigDecimal resultRefundAmount;



}
