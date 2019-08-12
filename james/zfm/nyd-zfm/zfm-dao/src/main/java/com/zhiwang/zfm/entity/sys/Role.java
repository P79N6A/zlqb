package com.zhiwang.zfm.entity.sys;

import io.swagger.annotations.ApiModelProperty;

public class Role {
		private String id;			//标识	private String name;			//角色名称	private java.util.Date createTime;			//创建时间
	private java.util.Date updateTime;			//修改时间	private Integer status;			//状态(1启用0停用)	private String sysModuleIds;			//菜单权限id(多个菜单,逗号隔开)	private String remark;			//描述
	
	@ApiModelProperty(value = "默认值 1 表示 不允许删除  0可删除")
	private String defaultFlag;			//默认值
		public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public String getSysModuleIds() {	    return this.sysModuleIds;	}	public void setSysModuleIds(String sysModuleIds) {	    this.sysModuleIds=sysModuleIds;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
}
