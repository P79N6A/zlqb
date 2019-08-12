package com.nyd.zeus.model.attendance;

import java.io.Serializable;

import com.nyd.zeus.model.common.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "考勤入参", description = "考勤入参")
public class AttendanceRequest extends PageCommon implements Serializable {

	private static final long serialVersionUID = 1687369012771122673L;
	
	@ApiModelProperty(value = "系统用户Id")
    private String sysUserId;
	
	@ApiModelProperty(value = "系统用户姓名")
    private String sysUserName;

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

}
