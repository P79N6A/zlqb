package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiu
 */
@Data
public class JxRepaymentsRequest extends JxCommonRequest implements Serializable {
    /**
     * 还款计划Id
     */
    private Long loanPhaseId;
    /**
     * 类型（1- 还款）；2-代偿
     */
    private Integer type;
    /**
     * 扣款金额
     */
    private BigDecimal requestAmount;
}
