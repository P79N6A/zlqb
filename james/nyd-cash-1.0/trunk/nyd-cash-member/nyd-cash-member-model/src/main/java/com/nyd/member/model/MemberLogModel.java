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
public class MemberLogModel implements Serializable {
    //用户ID
    private String userId;
    //会员id
    private String memberId;
    //订单编号
    private String orderNo;
    //会员类型
    private String memberType;
    //会员类型描述
    private String memberTypeDescribe;
    //会员折扣价
    private BigDecimal memberFee;
    //会员购买时间
    private Date startTime;
    //是否代扣成功 1 扣款成功，其他为错误码
    private String debitFlag;
    //扣款渠道
    private String debitChannel;
    //渠道名称（马甲包来源）
    private String appName;
}
