package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysDictionarySearchForm extends BaseSearchForm {
	
		private String id;			//主键	private String name;			//类型(大类)名称	private String code;			//类型(大类)编码	private Integer status;			//大类状态(1:可用 0:停用)	private String price;			//大类值	private java.util.Date createTime;			//创建时间	private java.util.Date updateTime;			//更新时间	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getCode() {	    return this.code;	}	public void setCode(String code) {	    this.code=code;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public String getPrice() {	    return this.price;	}	public void setPrice(String price) {	    this.price=price;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}
	
}
