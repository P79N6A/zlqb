package com.nyd.order.model;

import java.io.Serializable;
import java.util.Date;

public class THelibaoRecord implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;			//	private String orderNo;			//	private Integer status;			//0 初始化认证 1用户注册 2 正面资质上传 3 反面资质上传 4 全部认证成功	private String userNumber;			// 合利宝 返回编号
	private String userId;	private java.util.Date createTime;			//	private java.util.Date updateTime;			//	private Integer failCount;			//失败次数
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public THelibaoRecord(String id, String orderNo, Integer status, String userNumber, String userId, Date createTime,
			Date updateTime, Integer failCount) {
		super();
		this.id = id;
		this.orderNo = orderNo;
		this.status = status;
		this.userNumber = userNumber;
		this.userId = userId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.failCount = failCount;
	}
	public THelibaoRecord() {
		super();
		// TODO Auto-generated constructor stub
	}	
}
