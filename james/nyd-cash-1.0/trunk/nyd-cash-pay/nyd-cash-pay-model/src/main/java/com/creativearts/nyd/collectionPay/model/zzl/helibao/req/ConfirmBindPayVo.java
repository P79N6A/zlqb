package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.pay.api.annotation.zzl.FieldEncrypt;
import com.nyd.pay.api.annotation.zzl.SignExclude;


/**
 * Created by heli50 on 2017/4/15.
 */
@ApiModel(description="绑卡支付")
public class ConfirmBindPayVo{
	

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;

	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;

	@ApiModelProperty(value="商户订单号")
    private String p3_orderId;

	@ApiModelProperty(value="时间戳",hidden=true)
    private String p4_timestamp;

	@ApiModelProperty(value="短信验证码")
    @FieldEncrypt
    private String p5_validateCode;

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

	public String getP3_orderId() {
		return p3_orderId;
	}

	public void setP3_orderId(String p3_orderId) {
		this.p3_orderId = p3_orderId;
	}

	public String getP4_timestamp() {
		return p4_timestamp;
	}

	public void setP4_timestamp(String p4_timestamp) {
		this.p4_timestamp = p4_timestamp;
	}

	public String getP5_validateCode() {
		return p5_validateCode;
	}

	public void setP5_validateCode(String p5_validateCode) {
		this.p5_validateCode = p5_validateCode;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

    
}
