package com.nyd.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Dengw on 2017/12/6
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberConfigModel implements Serializable {
    //会员类型
    private String type;
    //会员类型描述
    private String typeDescribe;
    //会员实际金额
    private BigDecimal realFee;
    //会员折扣价
    private BigDecimal discountFee;
    //会员有效周期
    private Integer effectTime;
    //会员使用业务范围
    private String business;
}
