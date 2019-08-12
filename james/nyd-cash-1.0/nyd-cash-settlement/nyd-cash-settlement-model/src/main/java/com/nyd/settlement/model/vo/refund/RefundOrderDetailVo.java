package com.nyd.settlement.model.vo.refund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hwei
 * @create 2018-01-16 15:15
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefundOrderDetailVo {
    //期数
    private String curPeriod;
    //订单号
    private String orderNo;
    //账单号
    private String billNo;
    //姓名
    private String realName;
    //手机号
    private String mobile;
    //借款金额
    private BigDecimal repayPrinciple;
    //应还日期
    private Date promiseRepaymentDate;
    //应还本息（应实收金额）
    private BigDecimal receivableAmount;
    //还款日期（实际结清时间）
    private Date actualSettleDate;
    //实还金额
    private BigDecimal alreadyRepayAmount;
    //减免金额
    private BigDecimal couponDerateAmount;
    //应退款金额
    private BigDecimal refundAmount;
}
