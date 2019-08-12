package com.zhiwang.zfm.common.response.bean.order;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单详情-客户信息
 */
public class OrderCustInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "客户姓名")
	private String custName;
	@ApiModelProperty(value = "手机号码")
	private String mobile;
	@ApiModelProperty(value = "身份证号码")
	private String custIc;
	@ApiModelProperty(value = "单位名称")
	private String companyName;
	@ApiModelProperty(value = "单位地址")
	private String companyAddress;
	@ApiModelProperty(value = "常住地址")
	private String homeAddress;
	@ApiModelProperty(value = "月收入")
	private String monthlyIncome;
	@ApiModelProperty(value = "学历")
	private String education;
	@ApiModelProperty(value = "婚姻")
	private String marriage;
	@ApiModelProperty(value = "职业类型")
	private String jobType;
	@ApiModelProperty(value = "职业")
	private String job;
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCustIc() {
		return custIc;
	}
	public void setCustIc(String custIc) {
		this.custIc = custIc;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
}
