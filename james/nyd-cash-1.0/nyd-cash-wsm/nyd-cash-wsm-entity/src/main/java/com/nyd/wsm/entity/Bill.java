package com.nyd.wsm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/12/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bill {
    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //订单编号
    private String orderNo;

    //账单状态
    private String billStatus;

    //本次期数
    private Integer curPeriod;

    //总期数
    private Integer periods;

    //约定还款日期
    private Date promiseRepaymentDate;

    //实际结清时间
    private Date actualSettleDate;

    //本期应还金额
    private BigDecimal curRepayAmount;

    //本期应还本金
    private BigDecimal repayPrinciple;

    //本期应还利息
    private BigDecimal repayInterest;

    //剩余应还款金额
    private BigDecimal waitRepayAmount;

    //已还款金额
    private BigDecimal alreadyRepayAmount;

    //滞纳金
    private BigDecimal overdueFine;

    //逾期金额（罚息）
    private BigDecimal overdueAmount;

    //逾期天数
    private Integer overdueDays;

    //催收减免金额
    private BigDecimal collectionDerateAmount;

}
