package com.zhiwang.zfm.common.response.bean.sys;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
public class RoleVO  extends PageCommon{
		/**
	 * 
	 */
	private static final long serialVersionUID = -810217395311942507L;
	@ApiModelProperty(value = "主键标识")
	private String id;			//标识
	@ApiModelProperty(value = "角色名")	private String name;			//角色名称
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private java.util.Date createTime;			//创建时间
	@ApiModelProperty(value = "修改时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private java.util.Date updateTime;			//修改时间
	@ApiModelProperty(value = "状态:1有效,0删除")	private Integer status;			//状态(1启用0停用)
	@ApiModelProperty(value = "菜单权限IdList")	private String sysModuleIds;			//菜单权限id(多个菜单,逗号隔开)
	@ApiModelProperty(value = "备注")	private String remark;			//描述
	
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
