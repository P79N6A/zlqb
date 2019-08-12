package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import java.util.Date;

/**
 * 退单
 * Cong Yuxiang
 * 2017/11/22
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="t_chargeback_wsm")
public class ChargeBackWsm {

    @Id
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
    //服务利率
    private Double serviceRate;
    //服务费
    private Double serviceCost;

    private Date createTime;
    private Date updateTime;
    private String updateBy;


}
