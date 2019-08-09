package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by heli50 on 2017/4/14.
 */
@ApiModel(description="商户用户资质上传")
public class MerchantUserUploadVo {
	
	@ApiModelProperty(value="交易类型",hidden=true)
    private String P1_bizType;
	
	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;
	
	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p3_orderId;
	
	@ApiModelProperty(value="用户编号")
    private String p4_userId;
	
	@ApiModelProperty(value="时间戳",hidden=true)
    private String p5_timestamp;
	
	@ApiModelProperty(value="证件类型",hidden=true)
    private String p6_credentialType;
	
	@ApiModelProperty(value="文件签名",hidden=true)
    private String p7_fileSign;

	public String getP1_bizType() {
		return P1_bizType;
	}

	public void setP1_bizType(String p1_bizType) {
		P1_bizType = p1_bizType;
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

	public String getP4_userId() {
		return p4_userId;
	}

	public void setP4_userId(String p4_userId) {
		this.p4_userId = p4_userId;
	}

	public String getP5_timestamp() {
		return p5_timestamp;
	}

	public void setP5_timestamp(String p5_timestamp) {
		this.p5_timestamp = p5_timestamp;
	}

	public String getP6_credentialType() {
		return p6_credentialType;
	}

	public void setP6_credentialType(String p6_credentialType) {
		this.p6_credentialType = p6_credentialType;
	}

	public String getP7_fileSign() {
		return p7_fileSign;
	}

	public void setP7_fileSign(String p7_fileSign) {
		this.p7_fileSign = p7_fileSign;
	}

    
}
