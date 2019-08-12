package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "畅捷支付接口")
public class ChangJiePayVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "姓名")
	private String custName;

	@ApiModelProperty(value = "绑卡手机号")
	private String custMobile;

	@ApiModelProperty(value = "证件号")
	private String custIc;

	@ApiModelProperty(value = "卡号")
	private String cardNumber;

	@ApiModelProperty(value = "用户ID 不传")
	private String userID; // 用户ID 商户端用户的唯一编号

	@ApiModelProperty(value = "交易金额", required = true)
	private String amt; // 交易金额

	@ApiModelProperty(value = "通知路径", required = true)
	private String backUrl; // 通知路径

	@ApiModelProperty(value = "渠道", required = true)
	private String code; // 渠道 区分不同商户
	
	@ApiModelProperty(value = "保留字段")
	private String rem1; // 保留字段
	
	@ApiModelProperty(value = "短信验证码")
	private String smsCode; // 短信验证码
	
	
	

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getRem1() {
		return rem1;
	}

	public void setRem1(String rem1) {
		this.rem1 = rem1;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public String getCustIc() {
		return custIc;
	}

	public void setCustIc(String custIc) {
		this.custIc = custIc;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	

}
