package com.nyd.user.model.mq;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeFeeInfo {

    /**
     * 用户user_id
     */
    private String userId;

    /**手机号*/
    private String accountNumber;

    /**现金券id*/
    private String cashId;

    /**充值流水号 第三方流水号*/
    private String rechargeFlowNo;

    /** 用户付款金额*/
    private BigDecimal amount;

    /**充值是否成功 0：失败  1：成功*/
    private Integer operStatus;

    /**失败成功原因*/
    private String statusMsg;

    /**
     * 使用类型    1：用户充值收入 2：会员费收入  3：退现金券给用户收入  4：会员费支出',
     */
    private Integer userType;

    /**
     * 使用类型描述
     */
    private String cashDescription;

    /**
     * 现金券使用金额
     */
    private BigDecimal couponUseFee;

    //渠道名称（马甲包来源）
    private String appName;

}
