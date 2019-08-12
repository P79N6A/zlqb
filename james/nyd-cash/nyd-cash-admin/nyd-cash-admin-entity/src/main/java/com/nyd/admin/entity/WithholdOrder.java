package com.nyd.admin.entity;

import java.math.BigDecimal;

import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Table(name="t_withhold_order")
public class WithholdOrder {
    private String memberId;
    private String userId;
    private BigDecimal payAmount;
    private String payOrderNo;
    private String withholdOrderNo;
    private Integer orderStatus;
    private String appName;
}