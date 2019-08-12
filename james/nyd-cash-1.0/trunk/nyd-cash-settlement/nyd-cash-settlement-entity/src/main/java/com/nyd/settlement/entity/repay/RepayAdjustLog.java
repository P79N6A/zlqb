package com.nyd.settlement.entity.repay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * yuxiang.cong
 */
@Data
public class RepayAdjustLog {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String billNo;

    /**
     * 修改之前的状态
     */
    private String billStatusBe;

    /**
     * 修改之后的状态
     */
    private String billStatusAf;

    /**
     * 
     */
    private BigDecimal alreadyRepayAmountBe;

    /**
     * 
     */
    private BigDecimal alreadyRepayAmountAf;

    /**
     * 
     */
    private BigDecimal waitRepayAmountBe;

    /**
     * 
     */
    private BigDecimal waitRepayAmountAf;

    /**
     * 
     */
    private BigDecimal waitRepayPrincipleBe;

    /**
     * 修改之后的待收本金
     */
    private BigDecimal waitRepayPrincipleAf;

    /**
     * 修改之前的应收
     */
    private BigDecimal receivableAmountBe;

    /**
     * 修改之后的应收
     */
    private BigDecimal receivableAmountAf;

    private Date settleTimeBe;
    private Date settleTimeAf;

    /**
     * 修改之前的滞纳金
     */
    private BigDecimal overdueFineBe;

    /**
     * 修改之后的滞纳金
     */
    private BigDecimal overdueFineAf;

    /**
     * 修改前的罚息
     */
    private BigDecimal overdueAmountBe;

    /**
     * 修改后的罚息
     */
    private BigDecimal overdueAmountAf;

    private Integer overdueDayBe;
    private Integer overdueDayAf;

   }