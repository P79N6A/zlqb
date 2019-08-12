package com.nyd.member.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name="t_member_log")
public class MemberLog  implements Serializable{
    @Id
    private Long id;
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
    //会员费
    private BigDecimal memberFee;
    //会员购买时间
    private Date startTime;
    //是否代扣成功 "1"扣款成功 ，其他为失败码
    private String debitFlag;
    //扣款渠道
    private String debitChannel;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;

    //优惠券id
    private String couponId;
    //渠道名称（马甲包来源）
    private String appName;
}
