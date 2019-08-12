package com.zhiwang.zfm.common.response.bean.sys;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhiwang.zfm.common.request.PageCommon;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties
public class ModuleVO extends PageCommon{
		/**
	 * 
	 */
	private static final long serialVersionUID = 8715355384432101140L;
	@ApiModelProperty(value = "主键标识")
	private Integer id;			//标识
	@ApiModelProperty(value = "菜单名")	private String name;			//名称
	@ApiModelProperty(value = "父级Id")	private String pid;			//上级标识(id)
	@ApiModelProperty(value = "状态(1启用0停用)")	private Integer status;			//状态(1启用0停用)
	@ApiModelProperty(value = "排序")	private Integer sort;			//排序
	@ApiModelProperty(value = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")	private java.util.Date createTime;			//创建时间
	@ApiModelProperty(value = "备注")	private String remark;			//描述
	@ApiModelProperty(value = "是否拥有子节点(1拥有,0不拥有)")	private Integer hasNode;			//是否拥有子节点(1拥有,0不拥有)
	@ApiModelProperty(value = "菜单地址")	private String url;			//菜单地址
	@ApiModelProperty(value = "icon图标")	private String iconUrl;			//icon图标
	@ApiModelProperty(value = "下级资源")
	private List<ModuleVO> childrenModule;	//下级资源 	
		public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getPid() {	    return this.pid;	}	public void setPid(String pid) {	    this.pid=pid;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public Integer getSort() {	    return this.sort;	}	public void setSort(Integer sort) {	    this.sort=sort;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}	public Integer getHasNode() {	    return this.hasNode;	}	public void setHasNode(Integer hasNode) {	    this.hasNode=hasNode;	}	public String getUrl() {	    return this.url;	}	public void setUrl(String url) {	    this.url=url;	}	public String getIconUrl() {	    return this.iconUrl;	}	public void setIconUrl(String iconUrl) {	    this.iconUrl=iconUrl;	}
	public List<ModuleVO> getChildrenModule() {
		return childrenModule;
	}
	public void setChildrenModule(List<ModuleVO> childrenModule) {
		this.childrenModule = childrenModule;
	}
	
}
