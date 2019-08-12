package com.zhiwang.zfm.common.response.bean.activity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  InviteChannelConfigListVO  
 * @Description:  邀请好友活动渠道配置信息
 * @Author: taohui 
 * @CreateDate: 2018年12月1日 下午3:18:13 
 * @Version: v1.0  
 */
public class InviteChannelConfigListVO extends PageCommon implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="渠道配置id")
	private String channelId;			//标识
	
	@ApiModelProperty(value="更新人id")
	private String createSysUserId;			//更新人id
	
	@ApiModelProperty(value="更新人用户名")
	private String createSysUserName;			//更新人用户名
	
	@ApiModelProperty(value="分享渠道名称")
	private String channelName;			//分享渠道名称
	
	@ApiModelProperty(value="分享渠道APP ID")
	private String appid;			//分享渠道APP ID
	
	@ApiModelProperty(value="分享渠道APP secret/key")
	private String appsecretkey;			//分享渠道APP secret/key
	
	@ApiModelProperty(value="备注")
	private String remark;			//备注
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value="更新时间")
	private java.util.Date updateTime;			//更新时间

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCreateSysUserId() {
		return createSysUserId;
	}

	public void setCreateSysUserId(String createSysUserId) {
		this.createSysUserId = createSysUserId;
	}

	public String getCreateSysUserName() {
		return createSysUserName;
	}

	public void setCreateSysUserName(String createSysUserName) {
		this.createSysUserName = createSysUserName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsecretkey() {
		return appsecretkey;
	}

	public void setAppsecretkey(String appsecretkey) {
		this.appsecretkey = appsecretkey;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
}
