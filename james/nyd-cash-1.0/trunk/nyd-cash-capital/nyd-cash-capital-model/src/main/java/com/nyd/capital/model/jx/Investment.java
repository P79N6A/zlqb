package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Data
public class Investment implements Serializable {
    private String investorName;
    private String investorIdCardNumber;
    private BigDecimal amount;
}
