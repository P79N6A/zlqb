package com.zhiwang.zfm.common.response.bean.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *auther wanghu
 *date 2019年3月4日
 */
@ApiModel("生命周期首次统计返回")
public class ReportDownPaymentRespVo {
	
	@ApiModelProperty(value = "标识")
	private String id;			//标识
	
	@ApiModelProperty(value = "标识")
	private String runDate;			//统计日期
	
	@ApiModelProperty(value = "平台编码")
	private String platCode;			//平台编码
	
	@ApiModelProperty(value = "平台名称")
	private String platName;			//平台名称
	
	@ApiModelProperty(value = "首期注册数")
	private Integer registerCount;			//首期注册数
	
	@ApiModelProperty(value = "首期申请数")
	private Integer applyCount;			//首期申请数
	
	@ApiModelProperty(value = "首期通过数")
	private Integer passCount;			//首期通过数
	
	@ApiModelProperty(value = "首期放款数")
	private Integer loanCount;			//首期放款数
	
	@ApiModelProperty(value = "首期入催数")
	private Integer urgeCount;			//首期入催数
	
	@ApiModelProperty(value = "首期逾期数")
	private Integer overCount;			//首期逾期数
	
	@ApiModelProperty(value = "应还日期小于统计日期")
	private Integer todayCount;        //应还日期小于统计日期
	
	@ApiModelProperty(value = "首期放款金额")
	private java.math.BigDecimal loanSum;			//首期放款金额
	
	@ApiModelProperty(value = "首期入催率")
	private String urgeRate;
	
	@ApiModelProperty(value = "首期逾期率")
	private String overRate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
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

	public Integer getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(Integer registerCount) {
		this.registerCount = registerCount;
	}

	public Integer getApplyCount() {
		return applyCount;
	}

	public void setApplyCount(Integer applyCount) {
		this.applyCount = applyCount;
	}

	public Integer getPassCount() {
		return passCount;
	}

	public void setPassCount(Integer passCount) {
		this.passCount = passCount;
	}

	public Integer getLoanCount() {
		return loanCount;
	}

	public void setLoanCount(Integer loanCount) {
		this.loanCount = loanCount;
	}

	public Integer getUrgeCount() {
		return urgeCount;
	}

	public void setUrgeCount(Integer urgeCount) {
		this.urgeCount = urgeCount;
	}

	public Integer getOverCount() {
		return overCount;
	}

	public void setOverCount(Integer overCount) {
		this.overCount = overCount;
	}

	public java.math.BigDecimal getLoanSum() {
		return loanSum;
	}

	public void setLoanSum(java.math.BigDecimal loanSum) {
		this.loanSum = loanSum;
	}

	public String getUrgeRate() {
		return urgeRate;
	}

	public void setUrgeRate(String urgeRate) {
		this.urgeRate = urgeRate;
	}

	public String getOverRate() {
		return overRate;
	}

	public void setOverRate(String overRate) {
		this.overRate = overRate;
	}

	public Integer getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}
	
	
	

}
