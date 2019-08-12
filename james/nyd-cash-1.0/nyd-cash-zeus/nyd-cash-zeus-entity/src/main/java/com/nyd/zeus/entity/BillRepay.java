package com.nyd.zeus.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="t_bill_repay")
public class BillRepay {
		private String id;			//标识	private String billNo;			//账单编号	private String orderNo;			//订单号	private String userId;			//用户id	private String outOrderNumber;			//对外订单号	private java.math.BigDecimal repayAmount;			//还款金额	private String payType;			//还款方式(1:主动还款,2:跑批还款)	private java.util.Date createTime;			//交易时间	private java.util.Date endTime;			//交易结束时间	private String resultCode;			//交易状态(1:成功,2:失败,3:处理中)	private String remark;			//备注	public String getId() {	    return this.id;	}	public void setId(String id) {	    this.id=id;	}	public String getBillNo() {	    return this.billNo;	}	public void setBillNo(String billNo) {	    this.billNo=billNo;	}	public String getOrderNo() {	    return this.orderNo;	}	public void setOrderNo(String orderNo) {	    this.orderNo=orderNo;	}	public String getUserId() {	    return this.userId;	}	public void setUserId(String userId) {	    this.userId=userId;	}	public String getOutOrderNumber() {	    return this.outOrderNumber;	}	public void setOutOrderNumber(String outOrderNumber) {	    this.outOrderNumber=outOrderNumber;	}	public java.math.BigDecimal getRepayAmount() {	    return this.repayAmount;	}	public void setRepayAmount(java.math.BigDecimal repayAmount) {	    this.repayAmount=repayAmount;	}	public String getPayType() {	    return this.payType;	}	public void setPayType(String payType) {	    this.payType=payType;	}	public java.util.Date getCreateTime() {	    return this.createTime;	}	public void setCreateTime(java.util.Date createTime) {	    this.createTime=createTime;	}	public java.util.Date getEndTime() {	    return this.endTime;	}	public void setEndTime(java.util.Date endTime) {	    this.endTime=endTime;	}	public String getResultCode() {	    return this.resultCode;	}	public void setResultCode(String resultCode) {	    this.resultCode=resultCode;	}	public String getRemark() {	    return this.remark;	}	public void setRemark(String remark) {	    this.remark=remark;	}
}
