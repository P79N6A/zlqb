package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RemitReport {
    /**
     * 
     */
    private Integer id;

    /**
     * 系统 我方订单号
     */
    private String orderNo;

    /**
     * 放款号（与对方关联的号码）
     */
    private String remitNo;

    /**
     * 客户姓名
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
     * 手机号
     */
    private String mobile;

    /**
     * 开户行
     */
    private String depositBank;

    /**
     * 资金方名称(先空着)
     */
    private String capitalName;

    /**
     * 0测试标 1非测试标
     */
    private Integer testStatus;

    /**
     * 资方名称
     */
    private String remitChannel;

    /**
     * 放款来源
     */
    private String source;


    /**
     * 
     */
    private Date contractStartDate;

    /**
     * 
     */
    private Date contractEndDate;

    /**
     * 
     */
    private Integer borrowTime;

    /**
     * 借款金额
     */
    private BigDecimal borrowAmount;

    /**
     * 放款金额
     */
    private BigDecimal remitAmount;

    /**
     * 费用项（服务费+资方利息）
     */
    private BigDecimal feeItem;

    /**
     * 活动减免
     */
    private BigDecimal activityDerate;

    /**
     * 费用合计（费用项-活动减免）
     */
    private BigDecimal feeTotal;

    /**
     * 代转手续费  （放款渠道代付的手续费 提现手续费）
     */
    private BigDecimal feeTransfer;

    /**
     * 
     */
    private BigDecimal feeChannel;

    /**
     * 利率
     */
    private BigDecimal interestRate;

    /**
     * 利息
     */
    private BigDecimal interest;

    /**
     * 服务费
     */
    private BigDecimal feeService;

    /**
     * 
     */
    private String memberFeeId;

    /**
     * 
     */
    private BigDecimal memberFee;
    private String productCode;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private Integer debitCount;

    private BigDecimal memberFeePay;
    private Date memberFeePayDate;

    private BigDecimal memberFeeDrawBack;

}