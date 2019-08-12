package com.zhiwang.zfm.entity.sys;

public class CompanyUser {
		private String id;			//主键标识	private String userId;			//用户id	private String companyId;			//公司id(也是来自用户表)	private Integer status;			//状态(1,可用 0不可用)	private java.util.Date createTime;			//创建时间	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getUserId() {	    return this.userId;	}	public void setUserId(String userId) {	    this.userId=userId;	}	public String getCompanyId() {	    return this.companyId;	}	public void setCompanyId(String companyId) {	    this.companyId=companyId;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}
}
