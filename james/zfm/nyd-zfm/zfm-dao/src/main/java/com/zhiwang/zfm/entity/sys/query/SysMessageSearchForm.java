package com.zhiwang.zfm.entity.sys.query;

import com.zhiwang.zfm.common.page.BaseSearchForm;

public class SysMessageSearchForm extends BaseSearchForm {
	
		private String id;			//标识	private String custId;			//关联bg_customer	private String crmApplayId;			//关联crm_applay	private Integer enabled;			//是否有效(1有效 0无效)	private String msgContentId;			//关联loan_sys_message_content	private Integer readStatus;			//阅读状态(0未查看 1已查看)	private Integer nodeStatus;			//按钮文字显示(0不显示 1显示)	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getCustId() {	    return this.custId;	}	public void setCustId(String custId) {	    this.custId=custId;	}	public String getCrmApplayId() {	    return this.crmApplayId;	}	public void setCrmApplayId(String crmApplayId) {	    this.crmApplayId=crmApplayId;	}	public Integer getEnabled() {	    return this.enabled;	}	public void setEnabled(Integer enabled) {	    this.enabled=enabled;	}	public String getMsgContentId() {	    return this.msgContentId;	}	public void setMsgContentId(String msgContentId) {	    this.msgContentId=msgContentId;	}	public Integer getReadStatus() {	    return this.readStatus;	}	public void setReadStatus(Integer readStatus) {	    this.readStatus=readStatus;	}	public Integer getNodeStatus() {	    return this.nodeStatus;	}	public void setNodeStatus(Integer nodeStatus) {	    this.nodeStatus=nodeStatus;	}
	
}
