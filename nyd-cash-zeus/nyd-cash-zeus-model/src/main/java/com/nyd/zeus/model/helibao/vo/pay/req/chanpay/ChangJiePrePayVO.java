package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class ChangJiePrePayVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	 @ApiModelProperty(value="银行卡号",required=true)
	 private String  cardNumber;    //银行卡号
	 
	 @ApiModelProperty(value="渠道",required=false)
	 private String  code;    //渠道
	 
	 @ApiModelProperty(value="签约协议号",required=true)
	 private String  protocolno;    //签约协议号
	 
	 @ApiModelProperty(value="金额（元）",required=true)
	 private String  amt;    //金额（元）
	 
	 @ApiModelProperty(value="回调地址",required=false)
	 private String  callBackUrl;    //回调地址

	 
	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getProtocolno() {
		return protocolno;
	}

	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
	}



	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	 
	 

}
