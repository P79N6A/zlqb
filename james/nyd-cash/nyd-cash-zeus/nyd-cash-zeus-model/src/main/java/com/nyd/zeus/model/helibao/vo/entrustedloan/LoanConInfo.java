package com.nyd.zeus.model.helibao.vo.entrustedloan;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description="借款信息")
public class LoanConInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="loanTime")
	private String loanTime;
	
	@ApiModelProperty(value="借款时间单位")
	private String loanTimeUnit ;
	
	@ApiModelProperty(value="借款年利率")
	private String loanInterestRate;
	
	@ApiModelProperty(value="分期数")
	private String periodization ;
	
	@ApiModelProperty(value="每期天/月/年数")
	private String periodizationDays;
	
	@ApiModelProperty(value="每期金额")
	private String periodizationFee ;

	public String getLoanTime() {
		return loanTime;
	}

	public void setLoanTime(String loanTime) {
		this.loanTime = loanTime;
	}

	public String getLoanTimeUnit() {
		return loanTimeUnit;
	}

	public void setLoanTimeUnit(String loanTimeUnit) {
		this.loanTimeUnit = loanTimeUnit;
	}

	public String getLoanInterestRate() {
		return loanInterestRate;
	}

	public void setLoanInterestRate(String loanInterestRate) {
		this.loanInterestRate = loanInterestRate;
	}

	public String getPeriodization() {
		return periodization;
	}

	public void setPeriodization(String periodization) {
		this.periodization = periodization;
	}

	public String getPeriodizationDays() {
		return periodizationDays;
	}

	public void setPeriodizationDays(String periodizationDays) {
		this.periodizationDays = periodizationDays;
	}

	public String getPeriodizationFee() {
		return periodizationFee;
	}

	public void setPeriodizationFee(String periodizationFee) {
		this.periodizationFee = periodizationFee;
	}
	
	

}
