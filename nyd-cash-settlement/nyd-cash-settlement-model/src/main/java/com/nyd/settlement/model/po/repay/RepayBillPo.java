package com.nyd.settlement.model.po.repay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 2018/1/19
 */
@Data
public class RepayBillPo {
    //账单号
    private String billNo;
    //应还本金
    private BigDecimal repayPrinciple;
    //应还利息
    private BigDecimal repayInterest;
    //应还本息
    private BigDecimal curRepayAmount;
    //减免金额
    private BigDecimal couponDerateAmount;
    //剩余还款金额
    private BigDecimal waitRepayAmount;
    //挂账金额(已还金额)
    private BigDecimal alreadyRepayAmount;
    //约定还款日期
    private Date promiseRepaymentDate;
    //结清日期
    private Date actualSettleDate;
    //逾期天数
    private Integer overdueDays;
    //逾期罚息
    private BigDecimal overdueAmount;
    //滞纳金
    private BigDecimal overdueFine;
}
