package com.zhiwang.zfm.common.request.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("逾期报表分页查询请求")
public class ReportOverdueStatisticsReqVO extends ReportCommonReqVO {

	private static final long serialVersionUID = 1L;
	
	private Boolean lastDayOfStatistics;

	public Boolean getLastDayOfStatistics() {
		return lastDayOfStatistics;
	}

	public void setLastDayOfStatistics(Boolean lastDayOfStatistics) {
		this.lastDayOfStatistics = lastDayOfStatistics;
	}
	
}
