package com.zhiwang.zfm.common.response.bean.sys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
@ApiModel("菜单角色")
public class ModuleTreeVO extends PageCommon{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3499904858420742018L;
	@ApiModelProperty(value = "角色Id")
	private String roleId;
	@ApiModelProperty(value = "角色关联的菜单Id集合")
	private List<String> moduleIds;
	@ApiModelProperty(value = "下级资源")
	private List<ModuleVO> moduleList;	//下级资源 	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public List<String> getModuleIds() {
		return moduleIds;
	}
	public void setModuleIds(List<String> moduleIds) {
		this.moduleIds = moduleIds;
	}
	public List<ModuleVO> getModuleList() {
		return moduleList;
	}
	public void setModuleList(List<ModuleVO> moduleList) {
		this.moduleList = moduleList;
	}
	
}
