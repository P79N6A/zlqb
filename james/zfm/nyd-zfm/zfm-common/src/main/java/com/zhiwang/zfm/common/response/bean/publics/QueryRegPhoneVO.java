package com.zhiwang.zfm.common.response.bean.publics;

import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

public class QueryRegPhoneVO extends PageCommon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 640928321414723763L;
	@ApiModelProperty(value = "用户ID")
	private String userId;				//用户ID 
	@ApiModelProperty(value = "查询开始时间")
	/*@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")*/
	private String beginTime; // 创建时间
	@ApiModelProperty(value = "查询结束时间")
	/*@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")*/
	private String endTime; // 创建时间
	@ApiModelProperty(value = "渠道id")
	private String channelId; // 渠道id
	@ApiModelProperty(value = "渠道名称")
	private String channelName; // 渠道名称
	@ApiModelProperty(value = "子渠道id")
	private String childrenChannelId; // 子渠道id
	@ApiModelProperty(value = "子渠道名称")
	private String childrenChannelName; // 子渠道名称
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChildrenChannelName() {
		return childrenChannelName;
	}
	public void setChildrenChannelName(String childrenChannelName) {
		this.childrenChannelName = childrenChannelName;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getChildrenChannelId() {
		return childrenChannelId;
	}
	public void setChildrenChannelId(String childrenChannelId) {
		this.childrenChannelId = childrenChannelId;
	}
	
}
