package com.nyd.capital.model.qcgz;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class QcgzInvestorResponse implements Serializable {
    //投资人id
    private String investorId;
    //投资人姓名
    private String investorName;
    //放款金额
    private BigDecimal amount;

}
