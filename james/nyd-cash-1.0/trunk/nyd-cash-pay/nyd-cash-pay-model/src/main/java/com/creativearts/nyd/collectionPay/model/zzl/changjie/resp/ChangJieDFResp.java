package com.creativearts.nyd.collectionPay.model.zzl.changjie.resp;

import java.io.Serializable;

public class ChangJieDFResp implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 只看这个编码就好
	 * 0000成功
	 * 0001处理中
	 * 其余失败
	 */
	private String finalCode;
	/**
	 * 先判断这个字段如果是F直接失败
	 */
	private String acceptStatus; // 接口调用状态 S调用成功 F调用失败
	
	/**
	 * 000000 成功
	 * 000001 处理中
	 * 111111 失败
	 * 
	 * 900001  处理中 一小时后还为此状态为失败
	 * 900002  处理中 一小时后还为此状态为失败
	 * 900001   处理中 一小时后还为此状态为失败
	 * 
	 */
	private String originalRetCode; // 原交易返回码
	
	private String appRetcode; // 应用返回编码

	private String outTradeNo; // 流水号（查询交易用的流水号）
	
	private String fee; //手续费
	
	private String tradeDate;//交易时间yyyymmdd
	
	private String tradeTime;//交易时间hhmmss
	
	private String transAmt;//交易金额
	
	private String timeStamp;//交易时间
	
	private String flowNo; //交易流水号（畅捷返回的不需要使用）
	
	private String acctNo;//收款方银行卡号(被加密了。。。毫无作用)
	
	/**
	 *  0000成功
	 *  2000处理中
	 *  其余失败哦
	 */
	private String platformRetCode;//平台受理返回代码 0000成功
	/**
	 * 平台受理错误描述
	 */
	private String platformErrorMessage;//PlatformErrorMessage
	
	/**
	 * 应用返回信息
	 */
	private String appRetMsg;
	
	/**
	 * 原交易错误信息描述
	 */
	private String OriginalErrorMessage;

	
	public String getFinalCode() {
		return finalCode;
	}

	public void setFinalCode(String finalCode) {
		this.finalCode = finalCode;
	}

	public String getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public String getOriginalRetCode() {
		return originalRetCode;
	}

	public void setOriginalRetCode(String originalRetCode) {
		this.originalRetCode = originalRetCode;
	}

	public String getAppRetcode() {
		return appRetcode;
	}

	public void setAppRetcode(String appRetcode) {
		this.appRetcode = appRetcode;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getFlowNo() {
		return flowNo;
	}

	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getPlatformRetCode() {
		return platformRetCode;
	}

	public void setPlatformRetCode(String platformRetCode) {
		this.platformRetCode = platformRetCode;
	}

	public String getPlatformErrorMessage() {
		return platformErrorMessage;
	}

	public void setPlatformErrorMessage(String platformErrorMessage) {
		this.platformErrorMessage = platformErrorMessage;
	}

	public String getAppRetMsg() {
		return appRetMsg;
	}

	public void setAppRetMsg(String appRetMsg) {
		this.appRetMsg = appRetMsg;
	}

	public String getOriginalErrorMessage() {
		return OriginalErrorMessage;
	}

	public void setOriginalErrorMessage(String originalErrorMessage) {
		OriginalErrorMessage = originalErrorMessage;
	} 
	
	
}
