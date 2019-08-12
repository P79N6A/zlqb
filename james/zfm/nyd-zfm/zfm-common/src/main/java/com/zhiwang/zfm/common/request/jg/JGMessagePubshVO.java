package com.zhiwang.zfm.common.request.jg;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 极光推送类
 * @author psb
 *
 */
public class JGMessagePubshVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4376213564052983361L;
	
	@ApiModelProperty(value = "推送标题(app推送必填)")
	private String title;
	
	// 发送内容
	@ApiModelProperty(value = "发送内容(必填)")
	private String pushContent;

	
	@ApiModelProperty(value = "接收对象(手机号码)，如果为空表示推送全部会员")
	private String audience;


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getPushContent() {
		return pushContent;
	}


	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}


	public String getAudience() {
		return audience;
	}


	public void setAudience(String audience) {
		this.audience = audience;
	}
	
	
	
}
