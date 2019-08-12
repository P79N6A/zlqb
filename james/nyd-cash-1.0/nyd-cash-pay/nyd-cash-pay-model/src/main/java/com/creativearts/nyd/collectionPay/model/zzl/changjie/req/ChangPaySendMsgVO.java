package com.creativearts.nyd.collectionPay.model.zzl.changjie.req;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "发送短信验证码")
public class ChangPaySendMsgVO  implements Serializable{
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
	 
	 @ApiModelProperty(value="鉴权绑卡流水号",required=true)
	 private String  mchssn;    //鉴权绑卡流水号
	 
	 
	 @ApiModelProperty(value="流水号")
	 private String  serialNo;    //流水号 
	 
	 private String  code;    // 渠道  区分不同商户


	 
	public String getMchssn() {
		return mchssn;
	}


	public void setMchssn(String mchssn) {
		this.mchssn = mchssn;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
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


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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
	
	 
	
}
