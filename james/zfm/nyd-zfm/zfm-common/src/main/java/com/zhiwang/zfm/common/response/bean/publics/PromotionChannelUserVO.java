package com.zhiwang.zfm.common.response.bean.publics;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class PromotionChannelUserVO {

	@ApiModelProperty(value = "主键标识")	private String id;			//主键标识
	@ApiModelProperty(value = "用户id")	private String userId;			//用户id
	@ApiModelProperty(value = "渠道id")	private String channelId;			//渠道id
	@ApiModelProperty(value = "状态(1有效,0失效)")	private Integer status;			//状态(1有效,0失效)
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private java.util.Date createTime;			//创建时间
	@ApiModelProperty(value = "创建人id")	private String createSysUserId;			//创建人id	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getUserId() {	    return this.userId;	}	public void setUserId(String userId) {	    this.userId=userId;	}	public String getChannelId() {	    return this.channelId;	}	public void setChannelId(String channelId) {	    this.channelId=channelId;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public String getCreateSysUserId() {	    return this.createSysUserId;	}	public void setCreateSysUserId(String createSysUserId) {	    this.createSysUserId=createSysUserId;	}
}
