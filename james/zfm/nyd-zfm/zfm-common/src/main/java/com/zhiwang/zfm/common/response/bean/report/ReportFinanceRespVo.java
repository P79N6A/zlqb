package com.zhiwang.zfm.common.response.bean.report;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("财务统计报表返回")
public class ReportFinanceRespVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String platCode; // 平台编码
	@ApiModelProperty(value = "平台名称")
	private String platName; // 平台名称
	@ApiModelProperty(value = "统计日期(yyyy-MM-dd)")
	private String runDate; // 统计日期
	@ApiModelProperty(value = "总收入")
	private BigDecimal totalIncome; // 总收入
	@ApiModelProperty(value = "总支出")
	private BigDecimal totalExpenditure; // 总支出
	@ApiModelProperty(value = "放款支出")
	private BigDecimal loanExpenditure; // 放款支出
	@ApiModelProperty(value = "风险支出")
	private BigDecimal riskExpenditure; // 风险支出
	@ApiModelProperty(value = "短信支出")
	private BigDecimal smsExpenditure; // 短信支出
	@ApiModelProperty(value = "手续费支出")
	private BigDecimal feeExpenditure; // 手续费支出
	@ApiModelProperty(value = "其他支出")
	private BigDecimal otherExpenditure; // 其他支出

	/**
	 * 四舍五入
	 * 
	 * @param bd
	 * @return
	 */
	private BigDecimal setScale(BigDecimal bd) {
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
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

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = setScale(totalIncome);
	}

	public BigDecimal getTotalExpenditure() {
		return totalExpenditure;
	}

	public void setTotalExpenditure(BigDecimal totalExpenditure) {
		this.totalExpenditure = setScale(totalExpenditure);
	}

	public BigDecimal getLoanExpenditure() {
		return loanExpenditure;
	}

	public void setLoanExpenditure(BigDecimal loanExpenditure) {
		this.loanExpenditure = setScale(loanExpenditure);
	}

	public BigDecimal getRiskExpenditure() {
		return riskExpenditure;
	}

	public void setRiskExpenditure(BigDecimal riskExpenditure) {
		this.riskExpenditure = setScale(riskExpenditure);
	}

	public BigDecimal getSmsExpenditure() {
		return smsExpenditure;
	}

	public void setSmsExpenditure(BigDecimal smsExpenditure) {
		this.smsExpenditure = setScale(smsExpenditure);
	}

	public BigDecimal getFeeExpenditure() {
		return feeExpenditure;
	}

	public void setFeeExpenditure(BigDecimal feeExpenditure) {
		this.feeExpenditure = setScale(feeExpenditure);
	}

	public BigDecimal getOtherExpenditure() {
		return otherExpenditure;
	}

	public void setOtherExpenditure(BigDecimal otherExpenditure) {
		this.otherExpenditure = setScale(otherExpenditure);
	}
	
	

}
