package com.nyd.capital.model.jx;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liuqiu
 */
@Data
public class LoanPhase implements Serializable {
    /**
     * 借款合同ID
     */
    private Long Id;
    /**
     * 标Id
     */
    private String loanId;
    /**
     * 还款方式
     * 1-定期付息，到期还本
     * 2-一次性还本付息
     * 3-等额本息
     * 5-等本等息
     */
    private Integer repaymentType;
    /**
     * 借款期限
     */
    private Integer phaseCount;
    /**
     * 当前期数
     */
    private Integer number;
    /**
     *应还本金
     */
    private BigDecimal principal;
    /**
     * 应还利息
     */
    private BigDecimal interest;
    /**
     * 应还本息
     */
    private BigDecimal amount;
    /**
     * 应还日期
     */
    private Date dueAt;
    /**
     * 实还日期
     */
    private Date repaidAt;
    /**
     * 还款状态
     */
    private Integer status;
    /**
     * 计划代偿日
     */
    private Date expectGuaranteedAt;
    /**
     * 还款人Id
     */
    private Long repayerId;
    /**
     * 银行实还时间
     */
    private Date repayCompletedAt;
    /**
     * 手续费
     */
    private BigDecimal fee;
    /**
     * 分账手续费
     */
    private BigDecimal transferFee;
    /**
     * 原始合同列表
     */
    private List<Contract> contracts;
    /**
     * 收款确认函
     */
    private List<Contract> confirmContracts;
    /**
     * 债权转让协议
     */
    private List<Contract> assignContracts;
}
