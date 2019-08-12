package com.zhiwang.zfm.common.request.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("催收公司报表分页查询请求")
public class ReportCollectionCompanyStatisticsReqVO extends ReportCollectionStatisticsReqVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "催收公司")
	private String collectionCompany;
	public String getCollectionCompany() {
		return collectionCompany;
	}
	public void setCollectionCompany(String collectionCompany) {
		this.collectionCompany = collectionCompany;
	}
	

}
