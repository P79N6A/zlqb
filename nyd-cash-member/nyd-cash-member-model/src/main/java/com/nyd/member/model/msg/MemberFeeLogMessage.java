package com.nyd.member.model.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hwei on 2018/01/27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberFeeLogMessage implements Serializable {
    //用户ID
    private String userId; //有
    //会员id
    private String memberId; //没有
    //手机号
    private String mobile;//有
    //会员类型
    private String memberType;//有
    //订单编号
    private String orderNo; //没有
    //是否代扣成功 ，1 扣款成功,其他为错误码
    private String debitFlag;
    //扣款渠道
    private String debitChannel;

    //优惠券id
    private String couponId;

    //现金券使用金额
    private BigDecimal couponUseFee;

    //支付现金
    private BigDecimal payCash;
    //渠道名称（马甲包来源）
    private String appName;
}
