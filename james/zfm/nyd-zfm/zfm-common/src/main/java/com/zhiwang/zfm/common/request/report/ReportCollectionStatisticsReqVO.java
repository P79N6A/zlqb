package com.zhiwang.zfm.common.request.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("催收报表分页查询请求")
public class ReportCollectionStatisticsReqVO extends ReportCommonReqVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "客户类型0:新客,1:老客")
	private Integer custType;
	public Integer getCustType() {
		return custType;
	}
	public void setCustType(Integer custType) {
		this.custType = custType;
	}

}
