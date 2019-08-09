package com.nyd.order.model.YmtKzjrBill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class KzjrRepayDetail implements Serializable {


    //手机号
    private String accountNumber;

    //userId
    private String userId;

    //姓名
    private String userName;

    //身份证号
    private String idNumber;

    //订单号
    private String orderNo;

    //子订单号
    private String orderSno;

    //资产编号
//    private String assetNo;
    private String assetCode;

    //借款码
    private String borrowCode;

    //待还金额
    private BigDecimal repayAmount;

    //借款金额
    private BigDecimal borrowAmount;

    //逾期天数
    private Integer overdueDays;

    //约定还款时间
    private String repayTime;

    //滞纳金
    private BigDecimal overdueFine;

    //逾期金额（罚息）
    private BigDecimal overdueAmount;

    //应还利息
    private BigDecimal repayInterest;

    //借款期限
    private Integer borrowTime;

    //放款时间
    private String payTime;

    //借款时长单位（天，期）
    private String loanUnit;

    //app产品编码
    private String appCode;

    //app产品名称
    private String appName;

    //账单编号
    private String billNo;
}
