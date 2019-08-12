package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="t_pay_fee")
public class PayFee {
    @Id
    private Long id;
    //类型
    private String type;
    //类型描述
    private String typeDescribe;
    //实际金额
    private BigDecimal realFee;
    //折扣价
    private BigDecimal discountFee;
    //有效周期
    private Integer effectTime;
    //使用业务范围
    private String business;
    //是否已删除
    private Integer deleteFlag;
    //添加时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //修改人
    private String updateBy;
}