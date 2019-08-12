package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: wucx
 * @Date: 2018/9/5 14:11
 */
@Data
public class RechargePaymentRecordInfo implements Serializable {

    //充值付费时间
    private String createTime;

    //用户编号
    private String userId;

    //交易金额
    private BigDecimal transactionMoney;

    //交易渠道
    private String repayChannel;

    //已付现金
    private BigDecimal payCash;

    //小银卷
    private BigDecimal balanceUse;

    //使用现金券
    private BigDecimal returnBalanceFee;

    //优惠卷
    private BigDecimal couponFee;

    //会员编号
    private String memberId;

    //交易订单号
    private String repayNo;

    //交易款项
    private String repayType;

}
