package com.nyd.member.model;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberByCashCouponModel implements Serializable {
    @Id
    private Long id;
    //用户ID
    private String userId;
    //手机号
    private String mobile;
    //会员id
    private String memberId;
    //现金券使用金额
    private BigDecimal couponUseFee;

    //使用(账户充值现金券余额)中的金额
    private BigDecimal balanceUse;

    //使用(用户返回现金券余额)中的金额
    private BigDecimal returnBalanceUse;

    //'操作是否成功 0：成功 1：失败'
    private Integer status;

    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

    //支付现金
    private BigDecimal payCash;
    //渠道名称（马甲包来源）
    private String appName;
}
