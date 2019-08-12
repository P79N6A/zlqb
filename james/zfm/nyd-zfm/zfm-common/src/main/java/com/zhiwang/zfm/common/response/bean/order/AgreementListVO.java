package com.zhiwang.zfm.common.response.bean.order;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 产品订单信息
 */
public class AgreementListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "协议名称")
	private String agreementName;
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	@ApiModelProperty(value = "协议预览地址")
	private String agreementUrl;
	public String getAgreementName() {
		return agreementName;
	}
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAgreementUrl() {
		return agreementUrl;
	}
	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}

	
}
