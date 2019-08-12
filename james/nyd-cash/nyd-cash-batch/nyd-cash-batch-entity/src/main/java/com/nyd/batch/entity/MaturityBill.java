package com.nyd.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/19
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaturityBill {
    private Long id;
    private String userId;
    private String billNo;
    private String orderNo;
    private BigDecimal waitRepayAmount;

}
