package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiu
 */
@Data
public class JxQueryLoanPhasesRequest extends JxCommonRequest implements Serializable {
    private String loanId;
    private Integer status;
}
