package com.zhiwang.zfm.common.request;

import java.io.Serializable;

public class SmsRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3876240387479349067L;

	private String mobile;		// 手机号码
	
	private String number;		// 短信编号
	
	private String msgBody;		// 短信消息体

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	
}
