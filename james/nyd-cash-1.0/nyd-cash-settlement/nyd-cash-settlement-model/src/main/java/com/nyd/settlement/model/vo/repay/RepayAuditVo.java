package com.nyd.settlement.model.vo.repay;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
public class RepayAuditVo implements Serializable{
    private Long id;
    private String userName;
    private String mobile;
    private String userId;
    //账单号
    private String billNo;
    //还款日期
    private String repayDate;
    //还款金额
    private BigDecimal repayAmount;
    //还款渠道
    private String repayChannel;
    //交易流水
    private String transactionNo;
    // 0 不减免 1 利息 2 罚息 3 滞纳金 4 利息加罚息 5利息加罚息加滞纳金 6 其他
    private String derateType;
    //减免金额
    private String derateAmount;
    //代还人
    private String repayName;
    //代还人卡号
    private String repayBankNo;
    //备注
    private String remark;

    private Integer auditStatus;
}
