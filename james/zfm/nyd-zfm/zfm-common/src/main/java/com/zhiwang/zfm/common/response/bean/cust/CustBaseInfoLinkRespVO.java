package com.zhiwang.zfm.common.response.bean.cust;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 基本信息联系人集合
 */
public class CustBaseInfoLinkRespVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "平台编码")
	private String linkPlatCode;
	@ApiModelProperty(value = "平台名称")
	private String linkPlatName;
	@ApiModelProperty(value = "联系人关系")
	private String linkRelation;
	@ApiModelProperty(value = "联系人姓名")
	private String linkName;
	@ApiModelProperty(value = "联系电话")
	private String linkMobile;
	@ApiModelProperty(value = "现居住地址")
	private String linkUpdateTime;

	public String getLinkPlatCode() {
		return linkPlatCode;
	}

	public void setLinkPlatCode(String linkPlatCode) {
		this.linkPlatCode = linkPlatCode;
	}

	public String getLinkPlatName() {
		return linkPlatName;
	}

	public void setLinkPlatName(String linkPlatName) {
		this.linkPlatName = linkPlatName;
	}

	public String getLinkRelation() {
		return linkRelation;
	}

	public void setLinkRelation(String linkRelation) {
		this.linkRelation = linkRelation;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkMobile() {
		return linkMobile;
	}

	public void setLinkMobile(String linkMobile) {
		this.linkMobile = linkMobile;
	}

	public String getLinkUpdateTime() {
		return linkUpdateTime;
	}

	public void setLinkUpdateTime(String linkUpdateTime) {
		this.linkUpdateTime = linkUpdateTime;
	}

}
