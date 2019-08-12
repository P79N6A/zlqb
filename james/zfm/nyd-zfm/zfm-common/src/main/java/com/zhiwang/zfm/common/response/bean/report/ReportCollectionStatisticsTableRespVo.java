package com.zhiwang.zfm.common.response.bean.report;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("催收查询图表返回")
public class ReportCollectionStatisticsTableRespVo {


	@ApiModelProperty(value = "逾期天数")
	private String overdueDay;
	
	//全量客户数
	@ApiModelProperty(value = "全量客户数")
	private Integer allCustCount;			//逾期1天客户数
	//全量解决数
	@ApiModelProperty(value = "全量解决数")
	private Integer allSolveBillCount;			//解决数
	//全量解决率
	@ApiModelProperty(value = "全量解决率")
	private BigDecimal allSolveBillRate;			//解决率


	//新客客户数
	@ApiModelProperty(value = "新客客户数")
	private Integer newCustCount;			//逾期1天客户数
	//新客解决数
	@ApiModelProperty(value = "新客解决数")
	private Integer newSolveBillCount;			//解决数
	//新客解决率
	@ApiModelProperty(value = "新客解决率")
	private BigDecimal newSolveBillRate;			//解决率
	

	//老客客户数
	@ApiModelProperty(value = "老客客户数")
	private Integer oldCustCount;			//逾期1天客户数
	//老客解决数
	@ApiModelProperty(value = "老客解决数")
	private Integer oldSolveBillCount;			//解决数
	//老客解决率
	@ApiModelProperty(value = "老客解决率")
	private BigDecimal oldSolveBillRate;			//解决率
	public String getOverdueDay() {
		return overdueDay;
	}
	public void setOverdueDay(String overdueDay) {
		this.overdueDay = overdueDay;
	}
	public Integer getAllCustCount() {
		return allCustCount;
	}
	public void setAllCustCount(Integer allCustCount) {
		this.allCustCount = allCustCount;
	}
	public Integer getAllSolveBillCount() {
		return allSolveBillCount;
	}
	public void setAllSolveBillCount(Integer allSolveBillCount) {
		this.allSolveBillCount = allSolveBillCount;
	}
	public BigDecimal getAllSolveBillRate() {
		return allSolveBillRate;
	}
	public void setAllSolveBillRate(BigDecimal allSolveBillRate) {
		this.allSolveBillRate = allSolveBillRate;
	}
	public Integer getNewCustCount() {
		return newCustCount;
	}
	public void setNewCustCount(Integer newCustCount) {
		this.newCustCount = newCustCount;
	}
	public Integer getNewSolveBillCount() {
		return newSolveBillCount;
	}
	public void setNewSolveBillCount(Integer newSolveBillCount) {
		this.newSolveBillCount = newSolveBillCount;
	}
	public BigDecimal getNewSolveBillRate() {
		return newSolveBillRate;
	}
	public void setNewSolveBillRate(BigDecimal newSolveBillRate) {
		this.newSolveBillRate = newSolveBillRate;
	}
	public Integer getOldCustCount() {
		return oldCustCount;
	}
	public void setOldCustCount(Integer oldCustCount) {
		this.oldCustCount = oldCustCount;
	}
	public Integer getOldSolveBillCount() {
		return oldSolveBillCount;
	}
	public void setOldSolveBillCount(Integer oldSolveBillCount) {
		this.oldSolveBillCount = oldSolveBillCount;
	}
	public BigDecimal getOldSolveBillRate() {
		return oldSolveBillRate;
	}
	public void setOldSolveBillRate(BigDecimal oldSolveBillRate) {
		this.oldSolveBillRate = oldSolveBillRate;
	}
	
	
	
}
