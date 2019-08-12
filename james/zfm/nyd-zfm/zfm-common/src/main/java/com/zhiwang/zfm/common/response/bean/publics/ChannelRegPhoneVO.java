package com.zhiwang.zfm.common.response.bean.publics;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

public class ChannelRegPhoneVO extends PageCommon {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4117954225855211507L;
	
	@ApiModelProperty(value = "客户id")
	private String id; // 标识
	@ApiModelProperty(value = "客户姓名")
	private String custName; // 客户姓名
	@ApiModelProperty(value = "注册手机号")
	private String mobile; // 注册手机号
	@ApiModelProperty(value = "身份证号")
	private String ic; // 身份证号
	@ApiModelProperty(value = "注册时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private java.util.Date createTime; // 创建时间
	@ApiModelProperty(value = "注册渠道")
	private String regSource; // 注册渠道
	@ApiModelProperty(value = "渠道名称")
	private String channelName; // 渠道名称
	@ApiModelProperty(value = "父级注册渠道")
	private String parentRegSource; // 父级注册渠道
	@ApiModelProperty(value = "父级渠道名称")
	private String parentChannelName; // 父级渠道名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIc() {
		return ic;
	}
	public void setIc(String ic) {
		this.ic = ic;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getRegSource() {
		return regSource;
	}
	public void setRegSource(String regSource) {
		this.regSource = regSource;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getParentRegSource() {
		return parentRegSource;
	}
	public void setParentRegSource(String parentRegSource) {
		this.parentRegSource = parentRegSource;
	}
	public String getParentChannelName() {
		return parentChannelName;
	}
	public void setParentChannelName(String parentChannelName) {
		this.parentChannelName = parentChannelName;
	}
	
}
