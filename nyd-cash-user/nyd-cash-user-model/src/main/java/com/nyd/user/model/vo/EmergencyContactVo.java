package com.nyd.user.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class EmergencyContactVo implements Serializable {

	private static final long serialVersionUID = -2696690434063806913L;
	
	@ApiModelProperty(value = "用户Id")
	private String userId;
	
	@ApiModelProperty(value = "姓名")
	private String name;
	
	@ApiModelProperty(value = "手机号")
	private String phone;
	
	@ApiModelProperty(value = "联系人关系")
	private String relationMsg;
	
	@ApiModelProperty(value = "更新时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;
	
	@ApiModelProperty(value = "催收备注")
	private String remark;

	public String getRelationMsg() {
		return relationMsg;
	}

	public void setRelationMsg(String relationMsg) {
		this.relationMsg = relationMsg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "EmergencyContactVo [userId=" + userId + ", name=" + name + ", phone=" + phone + ", updateTime="
				+ updateTime + ", remark=" + remark + "]";
	}

}
