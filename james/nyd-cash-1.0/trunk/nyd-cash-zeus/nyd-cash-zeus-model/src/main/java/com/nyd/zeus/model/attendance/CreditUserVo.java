package com.nyd.zeus.model.attendance;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "user", description = "用户信息")
public class CreditUserVo implements Serializable {

	private static final long serialVersionUID = 7158093509993631340L;

	@ApiModelProperty(value = "系统用户Id")
	private String sysUserId;			

	@ApiModelProperty(value = "登录名")
	private String loginName;			

	@ApiModelProperty(value = "角色名称,出参")
	private String roleName;

	@ApiModelProperty(value = "状态:1正常,0禁用")
	private Integer status;

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}	

}
