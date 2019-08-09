package com.nyd.zeus.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public @Data
class ChargebackWsmVo implements Serializable{
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
    private String contractStartTime;
    //合同截止日
    private String contractEndTime;
    //对账日yyyyMMdd
    private String reconciliationDay;
    //借款天数
    private Integer borrowDays;
    //借款期数
    private Integer periodCount;
    //服务利率
    private Double serviceRate;
    //服务费
    private Double serviceCost;
}
