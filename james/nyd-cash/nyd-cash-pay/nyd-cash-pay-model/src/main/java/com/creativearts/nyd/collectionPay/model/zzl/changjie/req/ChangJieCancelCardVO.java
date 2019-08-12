package com.creativearts.nyd.collectionPay.model.zzl.changjie.req;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "畅捷取消绑卡接口")
public class ChangJieCancelCardVO  implements Serializable{
	private static final long serialVersionUID = 1L;
		
	 @ApiModelProperty(value="银行卡号",required=true)
	 private String  cardNumber;    //银行卡号
	 
	 @ApiModelProperty(value="渠道",required=true)
	 private String  code;    //渠道
	 
	 @ApiModelProperty(value="渠道",required=true)
	 private String  protocolno;    //签约协议号





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
