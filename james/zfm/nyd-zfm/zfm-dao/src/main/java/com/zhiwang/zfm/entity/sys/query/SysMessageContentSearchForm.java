package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysMessageContentSearchForm extends BaseSearchForm {
	
		private String id;			//标识	private String publishId;			//发布人 关联sys_employee	private String msgContent;			//消息内容	private String msgTitle;			//消息标题	private String nodeCode;			//节点编码	private String nodeName;			//节点名称	private String msgNode;			//按钮名称	private java.util.Date createTime;			//创建时间	private java.util.Date updateTime;			//更新时间	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getPublishId() {	    return this.publishId;	}	public void setPublishId(String publishId) {	    this.publishId=publishId;	}	public String getMsgContent() {	    return this.msgContent;	}	public void setMsgContent(String msgContent) {	    this.msgContent=msgContent;	}	public String getMsgTitle() {	    return this.msgTitle;	}	public void setMsgTitle(String msgTitle) {	    this.msgTitle=msgTitle;	}	public String getNodeCode() {	    return this.nodeCode;	}	public void setNodeCode(String nodeCode) {	    this.nodeCode=nodeCode;	}	public String getNodeName() {	    return this.nodeName;	}	public void setNodeName(String nodeName) {	    this.nodeName=nodeName;	}	public String getMsgNode() {	    return this.msgNode;	}	public void setMsgNode(String msgNode) {	    this.msgNode=msgNode;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}
	
}
