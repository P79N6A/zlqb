package com.nyd.admin.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RepayReport {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String orderNo;

    /**
     * 
     */
    private String customerName;

    /**
     * 
     */
    private String mobile;

    /**
     * 
     */
    private String loanNo;

    /**
     * 
     */
    private String capitalName;

    /**
     * 产品类型 1 单期
     */
    private Integer productType;

    /**
     * 
     */
    private String label;

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
     * 逾期天数  不逾期为0
     */
    private Integer overdueDay;

    /**
     * M0......
     */
    private String overduePeriod;

    /**
     * 借款天数 合同期限
     */
    private Integer contractTime;

    /**
     * 
     */
    private String serialNo;

    /**
     * 还款渠道
     */
    private String repayChannel;

    /**
     * 
     */
    private Date actualReceiptDay;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 
     */
    private BigDecimal remitAmount;

    /**
     * 
     */
    private BigDecimal feeItem;

    /**
     * 逾期应还金额
     */
    private BigDecimal overdueShouldAmount;

    /**
     * 本笔实收金额
     */
    private BigDecimal receiveAmount;

    /**
     * 累计还款金额
     */
    private BigDecimal accumRepayAmount;

    /**
     * 第三方手续费
     */
    private BigDecimal thirdPartyPoundage;

    /**
     * 本笔实收金额
     */
    private BigDecimal thisContractAmount;

    /**
     * 本笔实收服务费金额
     */
    private BigDecimal thisFeeService;

    /**
     * 本笔实收利息金额
     */
    private BigDecimal thisInterestAmount;

    /**
     * 本笔实收滞纳金
     */
    private BigDecimal thisFeeLate;

    /**
     * 本笔实收逾期罚息
     */
    private BigDecimal thisOverdueFaxi;

    /**
     * 代偿金额
     */
    private BigDecimal compensatoryAmount;

    /**
     * 活动减免
     */
    private BigDecimal activityDerate;

    /**
     * 
     */
    private BigDecimal collectionDerate;

    /**
     * 
     */
    private BigDecimal drawbackAmount;

    /**
     * 其他收入
     */
    private BigDecimal otherIncome;

    /**
     * 
     */
    private String memberFeeId;

    /**
     * 会员费 应付金额
     */
    private BigDecimal memberFeeAmount;

    /**
     * 会员费支付金额
     */
    private BigDecimal memberFeePay;

    /**
     * 会员费支付日期
     */
    private Date memberFeePayDate;

    /**
     * 
     */
    private String memberFeePayChannel;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;


}