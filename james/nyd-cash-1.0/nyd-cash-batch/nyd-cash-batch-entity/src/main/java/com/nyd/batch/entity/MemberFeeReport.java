package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberFeeReport {
    /**
     * 会员费id
     */
    private Integer id;

    /**
     * 
     */
    private Integer memberFeeId;

    /**
     * 
     */
    private String loanNo;

    /**
     * 
     */
    private String customerName;

    /**
     * 1 单期
     */
    private Integer productType;

    /**
     * 
     */
    private String label;

    /**
     * 手机日
     */
    private String mobile;

    /**
     * 0测试标 1非测试标
     */
    private Integer testStatus;

    /**
     * 
     */
    private Date contractStartDate;

    /**
     * 
     */
    private Date contractEndDate;

    /**
     * 借款期限
     */
    private Integer borrowTime;

    /**
     * 
     */
    private BigDecimal borrowAmount;

    /**
     * 
     */
    private BigDecimal remitAmount;

    /**
     * 
     */
    private BigDecimal memberFeePoundage;

    /**
     * 应付金额
     */
    private BigDecimal memberFeeAmount;

    /**
     * 
     */
    private BigDecimal memberFeePay;

    /**
     * 
     */
    private Date memberFeePayDate;

    /**
     * 合利宝
     */
    private String memberFeePayChannel;

    /**
     * 
     */
    private Date expireTime;

    /**
     * 退款金额
     */
    private BigDecimal drawbackAmount;

    /**
     * 
     */
    private String drawbackChannel;

    /**
     * 
     */
    private BigDecimal drawbackPoundage;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;


}