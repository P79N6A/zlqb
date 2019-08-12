package com.nyd.admin.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminRefundAmountInfo implements Serializable{
    //金额记录编码
    private String amountCode;
    //最小金额
    private BigDecimal minAmount;
    //最大金额
    private BigDecimal maxAmount;
    //状态0启用1禁用
    private Integer amountStatus;
    //注册数
    private Integer registerCount;
    
    private String amount;

    //页数
    private Integer pageNum;

    //每页个数
    private Integer pageSize;

}
