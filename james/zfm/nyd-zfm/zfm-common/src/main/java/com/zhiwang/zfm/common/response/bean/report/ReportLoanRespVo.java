package com.zhiwang.zfm.common.response.bean.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *auther wanghu
 *date 2019年3月4日
 */
@ApiModel("放款统计表返回")
public class ReportLoanRespVo {

	
	@ApiModelProperty(value = "标识")
	private String id;			//id
	
	@ApiModelProperty(value = "平台编码")
	private String platCode;			//平台编码
	
	@ApiModelProperty(value = "平台名称")
	private String platName;			//平台名称
	
	@ApiModelProperty(value = "统计日期")
	private String runDate;			//统计日期
	
	@ApiModelProperty(value = "放款数")
	private Integer loanCount;			//放款数
	
	@ApiModelProperty(value = "放款金额")
	private java.math.BigDecimal loanAmount;			//放款金额
	
	@ApiModelProperty(value = "实付金额")
	private java.math.BigDecimal actualAmount;			//实付金额
	
	@ApiModelProperty(value = "新客放款数")
	private Integer newPassengerLoan;			//新客放款数
	
	@ApiModelProperty(value = "新客放款金额")
	private java.math.BigDecimal newPassengerAmount;			//新客放款金额
	
	@ApiModelProperty(value = "复贷数")
	private Integer compoundLoanNumber;			//复贷数
	
	@ApiModelProperty(value = "复贷金额")
	private java.math.BigDecimal compoundLoanAmount;			//复贷金额
	
	@ApiModelProperty(value = "展期数")
	private Integer extensionNumber;			//展期数
	
	@ApiModelProperty(value = "备注")
	private String remark;			//备注

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public Integer getLoanCount() {
		return loanCount;
	}

	public void setLoanCount(Integer loanCount) {
		this.loanCount = loanCount;
	}

	public java.math.BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(java.math.BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public java.math.BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(java.math.BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Integer getNewPassengerLoan() {
		return newPassengerLoan;
	}

	public void setNewPassengerLoan(Integer newPassengerLoan) {
		this.newPassengerLoan = newPassengerLoan;
	}

	public java.math.BigDecimal getNewPassengerAmount() {
		return newPassengerAmount;
	}

	public void setNewPassengerAmount(java.math.BigDecimal newPassengerAmount) {
		this.newPassengerAmount = newPassengerAmount;
	}

	public Integer getCompoundLoanNumber() {
		return compoundLoanNumber;
	}

	public void setCompoundLoanNumber(Integer compoundLoanNumber) {
		this.compoundLoanNumber = compoundLoanNumber;
	}

	public java.math.BigDecimal getCompoundLoanAmount() {
		return compoundLoanAmount;
	}

	public void setCompoundLoanAmount(java.math.BigDecimal compoundLoanAmount) {
		this.compoundLoanAmount = compoundLoanAmount;
	}

	public Integer getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(Integer extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
