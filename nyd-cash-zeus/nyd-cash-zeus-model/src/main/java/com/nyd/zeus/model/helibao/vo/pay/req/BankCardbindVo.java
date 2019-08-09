package com.nyd.zeus.model.helibao.vo.pay.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

@ApiModel(description="用户绑定银行卡信息查询")
public class BankCardbindVo implements Serializable{
	
	

	@ApiModelProperty(value="交易类型",hidden=true)
	private String p1_bizType;
	
	@ApiModelProperty(value="商户编号",hidden=true)
	private String p2_customerNumber;
	
	@ApiModelProperty(value="用户id")
	private String p3_userId;
	
	@ApiModelProperty(value="绑定id")
	private String p4_bindId;
	
	@ApiModelProperty(value="时间戳",hidden=true)
	private String p5_timestamp;
	
	@ApiModelProperty(value="签名方式",hidden=true)
	@SignExclude
	private String signatureType;

	public String getP1_bizType() {
		return p1_bizType;
	}

	public void setP1_bizType(String p1_bizType) {
		this.p1_bizType = p1_bizType;
	}

	public String getP2_customerNumber() {
		return p2_customerNumber;
	}

	public void setP2_customerNumber(String p2_customerNumber) {
		this.p2_customerNumber = p2_customerNumber;
	}

	public String getP3_userId() {
		return p3_userId;
	}

	public void setP3_userId(String p3_userId) {
		this.p3_userId = p3_userId;
	}

	public String getP4_bindId() {
		return p4_bindId;
	}

	public void setP4_bindId(String p4_bindId) {
		this.p4_bindId = p4_bindId;
	}

	public String getP5_timestamp() {
		return p5_timestamp;
	}

	public void setP5_timestamp(String p5_timestamp) {
		this.p5_timestamp = p5_timestamp;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

	
}
