package com.zhiwang.zfm.common.response.bean.sys;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel(value = "user", description = "用户信息")
public class UserVO  extends PageCommon{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1888789080491562611L;
	@ApiModelProperty(value = "主键标识")
	private String id;			//
	/*@ApiModelProperty(value = "用户名")	private String name;*/			//
	@ApiModelProperty(value = "登录名")	private String loginName;			//
	@ApiModelProperty(value = "密码")
	private String password;			//
	/*@ApiModelProperty(value = "原密码")
	private String oldPassword;*/
	@ApiModelProperty(value = "角色IdList,入参")
	private List<String> roleIdList;
	@ApiModelProperty(value = "公司IdList,入参")
	//private List<String> companyIdList;
	private String companyIdList;
	@ApiModelProperty(value = "角色名称,出参")
	private String roleName;
	@ApiModelProperty(value = "公司名称,出参")
	private String companyName;
	@ApiModelProperty(value = "状态:1正常,0禁用")	private Integer status;			//
	@ApiModelProperty(value = "删除状态:1有效,0删除",hidden=true)
	private Integer delStatus;		//状态(1有效0删除)
	@ApiModelProperty(value = "状态名")
	private String statusMsg;
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private java.util.Date createTime;			//
	@ApiModelProperty(value = "备注")	private String remark;			//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
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
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getCompanyIdList() {
		return companyIdList;
	}
	public void setCompanyIdList(String companyIdList) {
		this.companyIdList = companyIdList;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}	
}
