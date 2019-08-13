package com.nyd.zeus.model.helibao;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billNo;//账单编号
	private String bindId;//合利宝绑卡id
	private String bindUserId;//合利宝绑卡用户id
	private String mobile;//用户手机号
	
	private BigDecimal payMoney;//金额
	private String settleStatus;//1:结清 ,2:未结清
	private String remark;//操作人
	
	private String requstType;//请求方式
	
	
	
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBindId() {
		return bindId;
	}
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	public String getBindUserId() {
		return bindUserId;
	}
	public void setBindUserId(String bindUserId) {
		this.bindUserId = bindUserId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRequstType() {
		return requstType;
	}
	public void setRequstType(String requstType) {
		this.requstType = requstType;
	}
	
	
	
}
