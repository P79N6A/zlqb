package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/8.
 * 订单信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderInfo implements Serializable {
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
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
    //约定还款时间
    private Date promiseRepaymentTime;
    //利息
    private BigDecimal interest;
    //综合费用
    private BigDecimal syntheticalFee;
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
    //审核详细原因
    private String auditReason;
    //借款申请时间
    private Date loanTime;
    //放款时间
    private Date payTime;
    //业务类型
    private String business;
    //资金渠道
    private String fundCode;
    //借款用途
    private String loanPurpose;
    //用户ID
    private String memberId;
    //会员费
    private BigDecimal memberFee;
    //会员类型
    private String memberType;
    //是否测试申请
    private Integer testStatus;
    //实际还款时间
    private Date actualRepaymentTime;
    //放款失败类型 0:默认值 1：系统放款失败 2：前端手工取消放款
    private Integer failType;
    //修改时间
    private Date updateTime;

    //放款失败时间
    private Date loanFailTime;

    /**
     * 订单的来源渠道（0 - 侬要贷， 1 - 银码头）
     */
    private Integer channel;

    /**
     * 银码头订单号
     * 如果为空则为侬要贷自己的订单
     */
    private String ibankOrderNo;

    /**
     * 机审还是人审(0：机审；1：人审)
     */
    private Integer whoAudit;
    private String assetNo;
    /**
     * 是否签约
     */
    private Integer ifSign;
    /**
     * 是否通知
     */
    private Integer ifNotice;
    /**
     * 是否推送资金风控0未推送1已推送
     */
    private Integer ifRisk;
    //马甲名称
    private String appName;
    private String payFailReason;

    //标识
    private String mark;

    private BigDecimal managerFee;
    
    private Integer loanNumber;  //借款期次
    private String reviewedId;//审核人id
    private String reviewedName;//审核人姓名
}
