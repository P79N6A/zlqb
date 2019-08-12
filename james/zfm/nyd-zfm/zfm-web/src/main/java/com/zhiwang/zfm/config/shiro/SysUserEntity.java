package com.zhiwang.zfm.config.shiro;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 系统用户
 * 
 * @author chenbo
 * @email 381756915@qq.com
 * @date 2018-01-5 19:22
 */
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String defaultPW="admin";

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空")
	private transient String password;

	
	/**
	 * 当前用户角色ID
	 */
	private String currentRoleId;
	
	
	/**
	 * 角色ID列表
	 */
	private List<String> roleIdList;

	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getCurrentRoleId() {
		return currentRoleId;
	}

	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}


	
	
	
}
