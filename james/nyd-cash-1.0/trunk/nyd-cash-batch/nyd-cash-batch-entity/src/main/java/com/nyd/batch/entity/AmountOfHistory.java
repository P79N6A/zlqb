package com.nyd.batch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/30
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AmountOfHistory {
    private BigDecimal repayAmountSum;
    private BigDecimal derateAmountSum;
}
