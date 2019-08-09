package com.nyd.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hwei on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "t_refund_amount")
public class RefundAmount implements Serializable{
    @Id
    private Long id;
    //金额记录编码
    private String amountCode;
    //最小金额
    private BigDecimal minAmount;
    //最大金额
    private BigDecimal maxAmount;
    //状态0启用1禁用
    private Integer amountStatus;
    //注册数
    private Integer registerCount;

}
