package com.nyd.capital.model.dld;

import java.io.File;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 借款请求提交参数
 * @author zhangdk
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanCommitParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1666440085585432232L;
	private String merOrderNo;
	private String cur;
	private String card;
	private String validityTime;
	private String cvv2;
	private String custom;
	private String phoneNo;
	private String certifyTp;
	private String certifyId;
	private String subject;
	private String body;
	private String ppFlag;
	private String loanAmt;
	private String charge;
	private String loanDays;
	private String loanTimeUnit;
	private String loanInterestrate;
	private String penaltyInterestrate;
	private String loanEndDate;
	private String purpose;
	private String periodization;
	private String periodizationDays;
	private String periodizationFee;
	private String tpMerId;
	private String userSignature;
	private String merCallBackUrl;
	private String customerId;
	private String bankName;
	private String branchBankName;
	private String contractTemplateNo;
	private String lateFeeInterestrate;
	private String prepaymentNotifyDay;
	private String prepaymentPenalty;
	private String preExpirationOverdueDays;
	private String preExpirationOverdueNo;

}
