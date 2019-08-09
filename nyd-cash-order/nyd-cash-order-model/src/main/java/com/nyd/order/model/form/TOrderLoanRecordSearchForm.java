package com.nyd.order.model.form;

import com.nyd.order.model.common.BaseSearchForm;


public class TOrderLoanRecordSearchForm extends BaseSearchForm {
	
		private String id;			//	private String seqNo;			//	private String orderNo;			//	private java.util.Date createTime;			//	private java.util.Date updateTime;			//	private Integer status;			//	private String remark;			//	private Integer type;			//1 代发 2 支付宝 3微信 4银行卡	private String sysUserId;			//操作人id
	private String userNo;	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getSeqNo() {	    return this.seqNo;	}	public void setSeqNo(String seqNo) {	    this.seqNo=seqNo;	}		public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}	public Integer getType() {	    return this.type;	}	public void setType(Integer type) {	    this.type=type;	}	public String getSysUserId() {	    return this.sysUserId;	}	public void setSysUserId(String sysUserId) {	    this.sysUserId=sysUserId;	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	
}
