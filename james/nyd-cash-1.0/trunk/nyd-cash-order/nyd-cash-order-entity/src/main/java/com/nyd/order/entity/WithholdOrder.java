package com.nyd.order.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Data
@ToString
@Table(name="t_withhold_order")
public class WithholdOrder implements Serializable {
    private String memberId;
    private String userId;
    private BigDecimal payAmount;
    private String payOrderNo;
    private String withholdOrderNo;
    private Integer orderStatus;
    private String appName;
    private String payType;
}
