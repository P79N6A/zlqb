package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiu
 */
@Data
public class JxQueryLoanPhasesResponse extends JxCommonResponse implements Serializable {
    /**
     * 记录条数
     */
    private Integer count;
    /**
     * 还款计划列表
     */
    private List<LoanPhase> items;
}
