package com.creativearts.nyd.collectionPay.model.zzl.changjie.resp;

import java.io.Serializable;

public class ChangJiePayCommonResp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 先判断这个字段如果是F直接失败
	 */
	private String acceptStatus; // 接口调用状态 S调用成功 F调用失败
	
	private String appRetMsg; // 应用返回描述
	
	private String appRetcode; // 应用返回编码

	private String orderTrxid; // 流水号
	/**
	 * 
	 */
	private String retCode; // 返回码
	/**
	 * 
	 */
	private String retMsg; // 返回描述
	/**
	 * 状态Status字段；如果该字段为S则可以判断业务状态为成功！ F：失败  P：处理中
	 * 如果业务状态为失败或者处理中可以获取RetCode与RetMsg获取详细信息。
	 */
	private String status; //
	
	private String trxId; // 我们传给畅捷的流水号
	
	/**
	 * 发短信 绑卡 解绑 使用  0000表示发短信 绑卡 解绑成功    其他或者为空为失败 取retcode和retmsg字段取code和描述
	 */
	private String bindStatus;
	
	/**
	 * 绑卡协议号 成功后存cust_bank_card 表 proto字段 （要存）
	 */
	private String proto;
	
	

	public String getProto() {
		return proto;
	}

	public void setProto(String proto) {
		this.proto = proto;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	public String getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public String getAppRetMsg() {
		return appRetMsg;
	}

	public void setAppRetMsg(String appRetMsg) {
		this.appRetMsg = appRetMsg;
	}

	public String getAppRetcode() {
		return appRetcode;
	}

	public void setAppRetcode(String appRetcode) {
		this.appRetcode = appRetcode;
	}

	public String getOrderTrxid() {
		return orderTrxid;
	}

	public void setOrderTrxid(String orderTrxid) {
		this.orderTrxid = orderTrxid;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}


}
