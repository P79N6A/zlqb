package com.nyd.order.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Data
@ToString
public class WithholdOrderInfo implements Serializable {
    private String memberId;
    private String userId;
    private BigDecimal payAmount;
    private String payOrderNo;
    private String withholdOrderNo;
    private Integer orderStatus;
    private Integer payType;
}
