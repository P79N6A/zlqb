package com.nyd.order.model.YmtKzjrBill;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillYmtInfo implements Serializable {

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //订单编号
    private String orderNo;

    //子订单编号
    private String orderSno;

    //资产编号
    private String assetCode;

    //账单状态
    private String billStatus;

    //约定还款日期
    private Date promiseRepaymentDate;

    //实际结清时间
    private Date actualSettleDate;

    //本次期数
    private Integer curPeriod;

    //总期数
    private Integer periods;

    //本期应还金额（本息和）
    private BigDecimal curRepayAmount;

    //本期应还本金
    private BigDecimal repayPrinciple;

    //本期应还利息
    private BigDecimal repayInterest;

    //应实收金额
    private BigDecimal receivableAmount;

    //剩余还款本金
    private BigDecimal waitRepayPrinciple;

    //剩余应还款金额
    private BigDecimal waitRepayAmount;

    //已还款金额
    private BigDecimal alreadyRepayAmount;

    //优惠券减免金额
    private BigDecimal couponDerateAmount;

    //退款金额
    private BigDecimal alreadyRefundAmount;

    //是否测试申请
    private Integer testStatus;

    //是否已删除
    private Integer deleteFlag;

    //添加时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //修改人
    private String updateBy;
}
