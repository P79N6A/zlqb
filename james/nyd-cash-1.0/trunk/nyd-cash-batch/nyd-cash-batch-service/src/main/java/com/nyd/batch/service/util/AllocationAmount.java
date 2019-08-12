package com.nyd.batch.service.util;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/30
 **/
@Data
public class AllocationAmount {
    private BigDecimal thisContractAmount = new BigDecimal(0);
    private BigDecimal thisInterestAmount = new BigDecimal(0);
    private BigDecimal thisFeeLate = new BigDecimal(0);
    private BigDecimal thisOverdueFaxi = new BigDecimal(0);
    private BigDecimal drawbackAmount = new BigDecimal(0);
}
