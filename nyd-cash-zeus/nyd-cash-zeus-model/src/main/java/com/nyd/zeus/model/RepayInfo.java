package com.nyd.zeus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepayInfo implements Serializable {

    //用户id
    private String userId;

    //账单编号
    private String billNo;

    //还款编号
    private String repayNo;

    //还款时间
    private Date repayTime;

    //还款金额
    private BigDecimal repayAmount;

    //还款姓名
    private String repayName;

    //还款身份证号
    private String repayIdNumber;

    //还款账号
    private String repayAccount;

    //支付通道
    private String repayChannel;

    //还款状态
    private String repayStatus;

    //支付宝用户ID
    private String userZfbId;

    //支付宝账户
    private String userZfbName;

    //还款方式
    private String repayType;

    //时间入账时间
    private Date actualRecordedTime;

    //第三方手续费
    private BigDecimal thirdProcedureFee;

    //优惠券id
    private String couponId;

    /**
     * 现金券使用金额
     */
    private BigDecimal couponUseFee;

    //渠道名称（马甲包来源）
    private String appName;

}
