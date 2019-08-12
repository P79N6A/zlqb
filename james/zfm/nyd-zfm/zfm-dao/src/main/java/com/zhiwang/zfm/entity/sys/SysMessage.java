package com.zhiwang.zfm.entity.sys;

public class SysMessage {
		private String id;			//标识	private String custInfoId;			//关联客户	private String orderId;			//关联订单	private Integer enabled;			//是否有效(1有效 0无效)	private String msgContentId;			//关联loan_sys_message_content	private Integer readStatus;			//阅读状态(0未查看 1已查看)	private Integer nodeStatus;			//按钮文字显示(0不显示 1显示)	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}		public String getCustInfoId() {
		return custInfoId;
	}
	public void setCustInfoId(String custInfoId) {
		this.custInfoId = custInfoId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Integer getEnabled() {	    return this.enabled;	}	public void setEnabled(Integer enabled) {	    this.enabled=enabled;	}	public String getMsgContentId() {	    return this.msgContentId;	}	public void setMsgContentId(String msgContentId) {	    this.msgContentId=msgContentId;	}	public Integer getReadStatus() {	    return this.readStatus;	}	public void setReadStatus(Integer readStatus) {	    this.readStatus=readStatus;	}	public Integer getNodeStatus() {	    return this.nodeStatus;	}	public void setNodeStatus(Integer nodeStatus) {	    this.nodeStatus=nodeStatus;	}
}
