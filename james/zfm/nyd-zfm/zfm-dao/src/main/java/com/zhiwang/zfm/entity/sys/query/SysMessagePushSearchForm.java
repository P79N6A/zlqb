package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysMessagePushSearchForm extends BaseSearchForm {
	
		private String id;			//标识	private String pushContent;			//发送内容	private String type;			//通知类型	private String typeCode;			//关联数据字典	private String operator;			//操作人	private java.util.Date createTime;			//创建时间	private java.util.Date updateTime;			//更新时间	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getPushContent() {	    return this.pushContent;	}	public void setPushContent(String pushContent) {	    this.pushContent=pushContent;	}	public String getType() {	    return this.type;	}	public void setType(String type) {	    this.type=type;	}	public String getTypeCode() {	    return this.typeCode;	}	public void setTypeCode(String typeCode) {	    this.typeCode=typeCode;	}	public String getOperator() {	    return this.operator;	}	public void setOperator(String operator) {	    this.operator=operator;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}
	
}
