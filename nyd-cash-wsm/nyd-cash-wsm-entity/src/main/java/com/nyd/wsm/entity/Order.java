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
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    //订单编号
    private String orderNo;
    //用户ID
    private String userId;
    //会员费
    private BigDecimal memberFee;
    //借款金额
    private BigDecimal loanAmount;
    //实际借款金额
    private BigDecimal realLoanAmount;
    //应还总金额
    private BigDecimal repayTotalAmount;
    //借款时间（天数）
    private Integer borrowTime;
    //借款期数
    private Integer borrowPeriods;
    //利息
    private BigDecimal interest;
    //年化利率
    private BigDecimal annualizedRate;
    //用户银行账户
    private String bankAccount;
    //银行名称
    private String bankName;
    //订单状态
    private Integer orderStatus;
    //审核结果
    private String auditStatus;
    //借款申请时间
    private Date loanTime;
    //放款时间
    private Date payTime;
}
