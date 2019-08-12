package com.nyd.capital.model.kzjr.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2017/12/13
 **/
@Data
public class KzjrInvestorResponse {
    private String investorId;
    private String investorName;
    private BigDecimal amount;
}
