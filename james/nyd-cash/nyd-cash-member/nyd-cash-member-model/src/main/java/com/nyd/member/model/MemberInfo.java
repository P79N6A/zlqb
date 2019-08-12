package com.nyd.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hwei on 2017/12/7.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfo implements Serializable{

    //手机号
    private String mobile;
    //会员状态
    private String memberStatus;
    //用户会员类型
    private String memberType;
    //会员类型描述
    private String memberTypeDescribe;
    //会员到期时间
    private String expireTime;
    //会员配置列表
    private List<MemberConfigModel> memberConfigList;

    //优惠券数量
    private Integer couponCount;

    //账户总余额
    private String totalBalance;

}
