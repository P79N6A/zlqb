package com.nyd.zeus.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 退单
 * deng qingfeng
 * 2019/8/5
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Table(name="t_payment_risk_exclude_info")
public class PaymentRiskExcludeInfo implements Serializable {
    @Id
    private Long id;
    //贷款编号
    private String orderNo;
    private Date createTime;
}
