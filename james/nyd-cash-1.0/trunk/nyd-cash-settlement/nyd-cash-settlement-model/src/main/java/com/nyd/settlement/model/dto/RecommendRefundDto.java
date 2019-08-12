package com.nyd.settlement.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class RecommendRefundDto implements Serializable{

    //订单号
    private String orderNo;

    //退款类型
    private Integer refundType;

    //客户编号
    private String userId;

    //姓名
    private String name;

    //手机号
    private String mobile;

    //退款金额
    private BigDecimal refundAmount;

    //退款方式
    private String refundChannel;

    //退款流水号
    private String refundFlowNo;

    //退款账户
    private String refundAccount;

    //退款原因
    private String refundReason;

    //退款时间
    private String refundTime;

    //退款手续费
    private BigDecimal refundFee;

    //退款状态
    private Integer refundStatus = 1;

}
