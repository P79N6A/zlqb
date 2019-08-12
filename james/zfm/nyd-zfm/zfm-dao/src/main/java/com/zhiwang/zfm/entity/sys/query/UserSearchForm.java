package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class UserSearchForm extends BaseSearchForm {
	
		private String id;			//	private String name;			//
	private String loginName;			//
	private String likeLoginName;			//	private String password;			//
	private Integer status;			//
	private Integer delStatus;			//	private java.util.Date createTime;			//	private String remark;			//	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getName() {	    return this.name;	}	public void setName(String name) {	    this.name=name;	}	public String getLoginName() {	    return this.loginName;	}	public void setLoginName(String loginName) {	    this.loginName=loginName;	}	public String getPassword() {	    return this.password;	}	public void setPassword(String password) {	    this.password=password;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getLikeLoginName() {
		return likeLoginName;
	}
	public void setLikeLoginName(String likeLoginName) {
		this.likeLoginName = likeLoginName;
	}
	
}
