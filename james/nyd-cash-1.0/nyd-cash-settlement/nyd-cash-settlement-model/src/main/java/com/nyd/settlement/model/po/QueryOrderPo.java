package com.nyd.settlement.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Peng
 * @create 2018-01-16 15:12
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryOrderPo{
    //产品类型
    private Integer productType;
    //测试标签
    private Integer testStatus;
    //订单编号
    private String orderNo;
    //会员费ID
    private Integer memberId;
    //姓名
    private String realName;
    //手机号
    private String mobile;
    //银行卡号
    private String bankAccount;
    //银行名称
    private String bankName;
    //资金渠道
    private String fundCode;
    //合同金额
    private BigDecimal realLoanAmount;
    //会员费
    private BigDecimal memberFee;
    //借款周期
    private Integer borrowPeriods;
    //借款日期
    private Date loanTime;
    //放款时间
    private Date payTime;
    //拒绝(拒绝)原因
    private String payFailReason;
}
