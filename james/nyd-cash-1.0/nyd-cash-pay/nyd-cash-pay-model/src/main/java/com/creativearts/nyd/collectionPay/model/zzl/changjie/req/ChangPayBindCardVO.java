package com.creativearts.nyd.collectionPay.model.zzl.changjie.req;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "确认绑卡接口")
public class ChangPayBindCardVO  implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="银行账户名称",required=true)
	 private String  custName;    //银行账户名称
	
	 @ApiModelProperty(value="银行卡号",required=true)
	 private String  cardNumber;    //银行卡号
	 
	@ApiModelProperty(value="证件号码",required=true)
	 private String custIC ;   //证件号码
	
	 @ApiModelProperty(value="客户手机号",required=true)
	 private String  custMobile;    //客户手机号
	 
	 @ApiModelProperty(value="客户手机号",required=true)
	 private String  userID;    //用户ID  商户端用户的唯一编号
	 
	 @ApiModelProperty(value="银行名称",required=true)
	 private String  bankName;    //银行名称
	 
	 @ApiModelProperty(value="短信验证码",required=true)
	 private String  msgCode;    //短信验证码
	 
	 @ApiModelProperty(value="渠道",required=false)
	 private String  code;    //渠道
	 
	 @ApiModelProperty(value="原鉴权流水号",required=false)
	 private String  mchntSsn;    //原鉴权流水号

	 
	public String getMchntSsn() {
		return mchntSsn;
	}

	public void setMchntSsn(String mchntSsn) {
		this.mchntSsn = mchntSsn;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
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


	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCustMobile() {
		return custMobile;
	}

	public void setCustMobile(String custMobile) {
		this.custMobile = custMobile;
	}

	public String getCustIC() {
		return custIC;
	}

	public void setCustIC(String custIC) {
		this.custIC = custIC;
	}


	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	 
	 
}
