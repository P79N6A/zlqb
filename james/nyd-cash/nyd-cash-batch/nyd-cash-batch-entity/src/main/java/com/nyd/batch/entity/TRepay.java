package com.nyd.batch.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TRepay {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户Id，逻辑主键
     */
    private String userId;

    /**
     * 还款编号
     */
    private String repayNo;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 还款时间
     */
    private Date repayTime;

    /**
     * 还款金额
     */
    private BigDecimal repayAmount;

    /**
     * 支付通道 0
     */
    private String repayChannel;

    /**
     * 支付宝 用户id
     */
    private String userZfbId;

    /**
     * 用户支付宝号
     */
    private String userZfbName;

    /**
     * 还款状态  0：成功；1：失败
     */
    private String repayStatus;

    /**
     * 还款方式 0，代扣；1主动还款
     */
    private Byte repayType;

    /**
     * 实际入账日
     */
    private Date actualRecordedTime;

    /**
     * 第三方手续费
     */
    private BigDecimal thirdProcedureFee;

    /**
     * 是否已删除 0：正常；1：已删除
     */
    private Byte deleteFlag;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 最后修改人
     */
    private String updateBy;
    /**
     * 0 利息 1罚息 2滞纳金 3 利息加罚息 4 利息加罚息加滞纳金 9其他
     */
    private Byte derateType;

    /**
     *
     */
    private BigDecimal derateAmount;

    /**
     *
     */
    private String remark;

   }