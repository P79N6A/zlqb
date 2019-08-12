package com.zhiwang.zfm.common.response.bean.report;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *auther wanghu
 *date 2019年3月7日
 */
@ApiModel("渠道生命周期返回")
public class ReportChannelLifeCycleRespVo {
	
	
	@ApiModelProperty(value = "状态")
	private String continuedLoanStatus;			//续贷状态(首期,XD1,XD2,XD3,XD4,XD5,XD6,XD7,XD8)
	
	@ApiModelProperty(value = "放款数")
	private Integer loanCount;			//放款数
	
	@ApiModelProperty(value = "入催数")
	private Integer urgeCount;			//入催数
	
	@ApiModelProperty(value = "逾期数")
	private Integer overCount;			//逾期数
	
	@ApiModelProperty(value = "应还日期小于统计日期")
	private Integer todayCount;			//当天的还款数
	
	@ApiModelProperty(value = "排序")
	private Integer xdSort;			//排序
	
	@ApiModelProperty(value = "续贷率")
	private String loanRate;
	
	@ApiModelProperty(value = "入催率")
	private String urgeRate;
	
	@ApiModelProperty(value = "逾期率")
	private String overRate;

	public String getContinuedLoanStatus() {
		return continuedLoanStatus;
	}

	public void setContinuedLoanStatus(String continuedLoanStatus) {
		this.continuedLoanStatus = continuedLoanStatus;
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

	public Integer getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(Integer todayCount) {
		this.todayCount = todayCount;
	}

	public Integer getXdSort() {
		return xdSort;
	}

	public void setXdSort(Integer xdSort) {
		this.xdSort = xdSort;
	}

	public String getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
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
	
	

}
