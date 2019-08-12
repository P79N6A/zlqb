package com.creativearts.nyd.collectionPay.model.zzl.helibao.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="交易卡信息列表")
public class BankCardbindBody {
	
	@ApiModelProperty(value="姓名")
	private  String payerName;
	
	@ApiModelProperty(value="证件类型")
	private  String idCardType;
	
	@ApiModelProperty(value="证件号")
	private  String idCardNo;
	
	@ApiModelProperty(value="卡号")
	private  String cardNo;
	
	@ApiModelProperty(value="银行编码")
	private  String bankId;
	
	@ApiModelProperty(value="借贷类型")
	private  String cardType;
	
	@ApiModelProperty(value="手机号")
	private  String phone;
	
	@ApiModelProperty(value="绑定状态")
	private  String bindStatus;
	
	@ApiModelProperty(value="绑定号")
	private  String bindId;

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	
	
}
