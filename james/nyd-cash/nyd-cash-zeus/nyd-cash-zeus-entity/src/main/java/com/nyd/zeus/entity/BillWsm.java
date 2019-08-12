package com.nyd.zeus.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="t_bill_wsm")
public class BillWsm {
//    @Id
    private Long id;
    //序号
    private Long serialNo;
    //商户
    private String merchant;
    //编号
    private String identifier;
    //订单编号
    private String orderNo;
    //姓名
    private String userName;
    //申请金额
    private Double amount;
    //商户订单号
    private String merchantOrderNo;
    //合同起始日
    private Date contractStartTime;
    //合同截止日
    private Date contractEndTime;
    //对账日yyyyMMdd
    private Date reconciliationDay;
    //借款天数
    private Integer borrowDays;
    //借款期数
    private Integer periodCount;
    //总服务费
    private Double serviceCostTotal;
    //wsm利率
    private Double wsmServiceRate;
    //wsm服务费
    private Double wsmServiceCost;
    //银行服务费
    private Double bankServiceCost;
    //银行利率
    private Double bankServiceRate;

    //第三方服务利率
    private Double extraServiceRate;
    //第三方服务费
    private Double extraServiceCost;

    //服务费还款日yyyyMMdd
    private Date serviceCostPayday;

    //总利息
    private Double interestTotal;

    //应还款日yyyyMMdd
    private Date payday;


    private Date createTime;
    private Date updateTime;
    private String updateBy;


}
