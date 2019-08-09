package com.nyd.order.model.form;

import com.nyd.order.model.common.BaseSearchForm;


public class THelibaoRecordSearchForm extends BaseSearchForm {
	
		private String id;			//	private String orderNo;			//	private Integer status;			//0 初始化认证 1用户注册 2 正面资质上传 3 反面资质上传 4 全部认证成功	private String userNumber;			//	private java.util.Date createTime;			//	private java.util.Date updateTime;			//	private Integer failCount;			//失败次数	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getOrderNo() {	    return this.orderNo;	}	public void setOrderNo(String orderNo) {	    this.orderNo=orderNo;	}	public Integer getStatus() {	    return this.status;	}	public void setStatus(Integer status) {	    this.status=status;	}	public String getUserNumber() {	    return this.userNumber;	}	public void setUserNumber(String userNumber) {	    this.userNumber=userNumber;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getUpdateTime() {	    return this.updateTime;	}	public void setUpdateTime(java.util.Date updateTime) {	    this.updateTime=updateTime;	}	public Integer getFailCount() {	    return this.failCount;	}	public void setFailCount(Integer failCount) {	    this.failCount=failCount;	}
	
}
