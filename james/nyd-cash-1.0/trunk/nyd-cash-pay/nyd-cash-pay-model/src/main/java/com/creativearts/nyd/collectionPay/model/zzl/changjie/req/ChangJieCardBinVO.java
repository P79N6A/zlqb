package com.creativearts.nyd.collectionPay.model.zzl.changjie.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  畅捷卡bin查询
 * @author admin
 *
 */
@ApiModel(description="畅捷卡bin查询接口")
public class ChangJieCardBinVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="银行卡号")
	private String cardNum; //卡号

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	


	
}
