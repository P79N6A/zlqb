package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class ModuleSearchForm extends BaseSearchForm {
	
		private Integer id;			//标识
	private String name;			//名称
	private String likeName;			//模糊查询名称	private String pid;			//上级标识(id)	private Integer status;			//状态(1启用0停用)	private Integer sort;			//排序	private java.util.Date createTime;			//创建时间	private String remark;			//描述	private Integer hasNode;			//是否拥有子节点(1拥有,0不拥有)	private String url;			//菜单地址	private String iconUrl;			//icon图标	public Integer getId() {	    return this.id;	}	public void setId(Integer id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getPid() {	    return this.pid;	}	public void setPid(String pid) {	    this.pid=pid;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public Integer getSort() {	    return this.sort;	}	public void setSort(Integer sort) {	    this.sort=sort;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}	public Integer getHasNode() {	    return this.hasNode;	}	public void setHasNode(Integer hasNode) {	    this.hasNode=hasNode;	}	public String getUrl() {	    return this.url;	}	public void setUrl(String url) {	    this.url=url;	}	public String getIconUrl() {	    return this.iconUrl;	}	public void setIconUrl(String iconUrl) {	    this.iconUrl=iconUrl;	}
	public String getLikeName() {
		return likeName;
	}
	public void setLikeName(String likeName) {
		this.likeName = likeName;
	}
}
