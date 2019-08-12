package com.nyd.settlement.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 评估费查询扎展示的实体类
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValuationFeePo {

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

    //评估费
    private BigDecimal repayAmount;

    //支付渠道
    private String repayChannel;

    //支付日期
    private Date repayTime;

    //支付手续费
    private BigDecimal thirdProcedureFee;

    //已退金额（该字段不需要，评估费不退款）
    private BigDecimal refundAmount;




}
