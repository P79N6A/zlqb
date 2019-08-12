package com.zhiwang.zfm.common.response.bean.cust;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class CustOrderDetailRespVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String platCode;
	@ApiModelProperty(value = "平台名称")
	private String platName;
	@ApiModelProperty(value = "订单号")
	private String orderNumber;
	@ApiModelProperty(value = "借款期次")
	private String applyNum;
	@ApiModelProperty(value = "放款期次")
	private String loanNum;
	@ApiModelProperty(value = "展期期次")
	private String extensionNum;
	@ApiModelProperty(value = "借款金额(元)")
	private String loanMoney;
	@ApiModelProperty(value = "借款期限（天）")
	private String period;
	@ApiModelProperty(value = "借款时间")
	private String createTime;
	@ApiModelProperty(value = "实际到账金额(元)")
	private String actualMoney;
	@ApiModelProperty(value = "实际还款金额(元)")
	private String repaymentMoney;
	@ApiModelProperty(value = "状态")
	private String orderStatus;
	@ApiModelProperty(value = "状态说明")
	private String orderStatusDesc;

	public String getPlatCode() {
		return platCode;
	}

	public void setPlatCode(String platCode) {
		this.platCode = platCode;
	}

	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(String applyNum) {
		this.applyNum = applyNum;
	}

	public String getLoanNum() {
		return loanNum;
	}

	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}

	public String getExtensionNum() {
		return extensionNum;
	}

	public void setExtensionNum(String extensionNum) {
		this.extensionNum = extensionNum;
	}

	public String getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(String loanMoney) {
		this.loanMoney = loanMoney;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(String actualMoney) {
		this.actualMoney = actualMoney;
	}

	public String getRepaymentMoney() {
		return repaymentMoney;
	}

	public void setRepaymentMoney(String repaymentMoney) {
		this.repaymentMoney = repaymentMoney;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

}
