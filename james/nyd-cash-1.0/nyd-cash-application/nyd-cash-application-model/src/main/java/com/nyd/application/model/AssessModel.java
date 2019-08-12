package com.nyd.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Dengw on 2018/2/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssessModel implements Serializable {
    //评估费用
    private BigDecimal assessFee;
    //评估费原价
    private BigDecimal realFee;
    //评估费类型
    private String type;
    //优惠券数量
    private Integer couponCount;
    //评估分等级
    private String preAuditLevel;
    //手机号
    private String mobile;
    //账户总余额
    private String totalBalance;

}
