package com.nyd.settlement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/8.
 * 订单
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_order")
public class Order {
    //主键id
    @Id
    private Long id;
    //订单编号
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
    //借款用途
    private String loanPurpose;
    //会员类型
    private String memberType;
    //会员费
    private BigDecimal memberFee;
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
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}
