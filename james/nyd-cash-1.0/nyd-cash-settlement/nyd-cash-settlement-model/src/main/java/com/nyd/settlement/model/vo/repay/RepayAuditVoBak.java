package com.nyd.settlement.model.vo.repay;

import lombok.Data;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Data
public class RepayAuditVoBak implements Serializable{
    private String productType;
    private String label;
    private String capitalName;
    private String orderNo;
    private String memberFeeId;
    private String name;
    private String mobile;
    private String memberFee;
    private String contractAmount;//合同金额
    private String borrowTime;//借款周期
    private String remitDate;//放款日期
    private String shouldRepayDate;//应还日期
    private String actualRepayDate;//实际还款日期
    private String shouldPrincipalInterest;//应还本息
    private String derateAmount;//减免金额
    private Integer overdueDays;//逾期天数
    private String remark;
}
