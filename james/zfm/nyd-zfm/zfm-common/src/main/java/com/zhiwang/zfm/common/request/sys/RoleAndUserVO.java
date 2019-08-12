package com.zhiwang.zfm.common.request.sys;

import java.util.List;

public class RoleAndUserVO {
	private String userId;			
	private String userName;			
	private String roleId;			
	private String roleName;
	private List<String> ignoreUserIdList;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public List<String> getIgnoreUserIdList() {
		return ignoreUserIdList;
	}
	public void setIgnoreUserIdList(List<String> ignoreUserIdList) {
		this.ignoreUserIdList = ignoreUserIdList;
	}
	
}
