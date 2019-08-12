package com.zhiwang.zfm.common.response.bean.order;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单详情-产品信息
 */
public class OrderProductInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单编号")
	private String orderNo;
	@ApiModelProperty(value = "申请时间")
	private String applayTime;
	@ApiModelProperty(value = "申请金额")
	private String applayAmount;
	@ApiModelProperty(value = "申请期限")
	private String applayPeriods;
	@ApiModelProperty(value = "产品利率")
	private String productRate;
	@ApiModelProperty(value = "放款产品")
	private String prodcutName;
	@ApiModelProperty(value = "借款用途")
	private String purpose;
	@ApiModelProperty(value = "放款时间")
	private String loanTime;
	@ApiModelProperty(value = "放款金额")
	private String loanAmount;
	@ApiModelProperty(value = "应还日期")
	private String repaymentTime;
	@ApiModelProperty(value = "应还本金")
	private String shouldPrincipal;
	@ApiModelProperty(value = "应还利息")
	private String shouldInterest;
	@ApiModelProperty(value = "应还服务费")
	private String shouldServiceFee;
	@ApiModelProperty(value = "实收评估费用")
	private String assessmentAmount;
	@ApiModelProperty(value = "结清日期")
	private String settleTime;
	@ApiModelProperty(value = "逾期罚息")
	private String overdueInterest;
	@ApiModelProperty(value = "已还金额")
	private String alreadyAmount;
	@ApiModelProperty(value = "剩余应还")
	private String remainsAmount;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getApplayTime() {
		return applayTime;
	}
	public void setApplayTime(String applayTime) {
		this.applayTime = applayTime;
	}
	public String getApplayAmount() {
		return applayAmount;
	}
	public void setApplayAmount(String applayAmount) {
		this.applayAmount = applayAmount;
	}
	public String getApplayPeriods() {
		return applayPeriods;
	}
	public void setApplayPeriods(String applayPeriods) {
		this.applayPeriods = applayPeriods;
	}
	public String getProductRate() {
		return productRate;
	}
	public void setProductRate(String productRate) {
		this.productRate = productRate;
	}
	public String getProdcutName() {
		return prodcutName;
	}
	public void setProdcutName(String prodcutName) {
		this.prodcutName = prodcutName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getLoanTime() {
		return loanTime;
	}
	public void setLoanTime(String loanTime) {
		this.loanTime = loanTime;
	}
	public String getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}
	public String getRepaymentTime() {
		return repaymentTime;
	}
	public void setRepaymentTime(String repaymentTime) {
		this.repaymentTime = repaymentTime;
	}
	public String getShouldPrincipal() {
		return shouldPrincipal;
	}
	public void setShouldPrincipal(String shouldPrincipal) {
		this.shouldPrincipal = shouldPrincipal;
	}
	public String getShouldInterest() {
		return shouldInterest;
	}
	public void setShouldInterest(String shouldInterest) {
		this.shouldInterest = shouldInterest;
	}
	public String getShouldServiceFee() {
		return shouldServiceFee;
	}
	public void setShouldServiceFee(String shouldServiceFee) {
		this.shouldServiceFee = shouldServiceFee;
	}
	public String getAssessmentAmount() {
		return assessmentAmount;
	}
	public void setAssessmentAmount(String assessmentAmount) {
		this.assessmentAmount = assessmentAmount;
	}
	public String getSettleTime() {
		return settleTime;
	}
	public void setSettleTime(String settleTime) {
		this.settleTime = settleTime;
	}
	public String getOverdueInterest() {
		return overdueInterest;
	}
	public void setOverdueInterest(String overdueInterest) {
		this.overdueInterest = overdueInterest;
	}
	public String getAlreadyAmount() {
		return alreadyAmount;
	}
	public void setAlreadyAmount(String alreadyAmount) {
		this.alreadyAmount = alreadyAmount;
	}
	public String getRemainsAmount() {
		return remainsAmount;
	}
	public void setRemainsAmount(String remainsAmount) {
		this.remainsAmount = remainsAmount;
	}
	
	
}
