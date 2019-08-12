package com.nyd.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Dengw on 2017/12/6
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberModel implements Serializable {
    //用户ID
    private String userId;
    //会员id
    private String memberId;
    //手机号
    private String mobile;
    //会员类型
    private String memberType;
    //订单编号
    private String orderNo;
    //是否代扣成功  1 扣款成功,其他为错误码
    private String debitFlag;
    //扣款渠道
    private String debitChannel;
    //会员类型描述
    private String memberTypeDescribe;
    //到期时间
    private Date expireTime;
    //会员有效期
    private Integer effectTime;

    private BigDecimal memberFee;

    //优惠券id
    private String couponId;
    //渠道名称（马甲包来源）
    private String appName;

}
