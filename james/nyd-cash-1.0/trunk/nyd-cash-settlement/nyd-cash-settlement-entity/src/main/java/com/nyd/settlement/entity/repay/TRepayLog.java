package com.nyd.settlement.entity.repay;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * yuxiang.cong
 */
@Data
public class TRepayLog {
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
     * 还款姓名
     */
    private String repayName;

    /**
     * 还款身份证号
     */
    private String repayIdNumber;

    /**
     * 还款账号
     */
    private String repayAccount;

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
     * 0强扣,1使用第二种方法还款,2用绑定的卡还款,3会员费,10非以上任何一种,11会员费失败,12强扣失败,13使用第二种方式还款失败,14用绑定的卡还款失败,15未知失败
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