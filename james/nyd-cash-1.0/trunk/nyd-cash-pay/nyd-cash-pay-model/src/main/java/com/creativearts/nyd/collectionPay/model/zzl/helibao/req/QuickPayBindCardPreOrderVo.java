package com.creativearts.nyd.collectionPay.model.zzl.helibao.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.pay.api.annotation.zzl.FieldEncrypt;
import com.nyd.pay.api.annotation.zzl.SignExclude;


/**
 * Created by mask on 2019/3/18
 */
@ApiModel(description="绑卡预下单")
public class QuickPayBindCardPreOrderVo{
	

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;

	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;

	@ApiModelProperty(value="用户id",hidden=true)
    private String p3_userId;

	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p4_orderId;

	@ApiModelProperty(value="时间戳",hidden=true)
    private String p5_timestamp;

	@ApiModelProperty(value="姓名")
    private String p6_payerName;

	@ApiModelProperty(value="证件类型",hidden=true)
    private String p7_idCardType;

	@ApiModelProperty(value="证件号码")
    @FieldEncrypt
    private String p8_idCardNo;

	@ApiModelProperty(value="银行卡号")
    @FieldEncrypt
    private String p9_cardNo;

	@ApiModelProperty(value="信用卡有效期年份",hidden=true)
    @FieldEncrypt
    private String p10_year;

	@ApiModelProperty(value="信用卡有效期月份",hidden=true)
    @FieldEncrypt
    private String p11_month;

	@ApiModelProperty(value="cvv2",hidden=true)
    @FieldEncrypt
    private String p12_cvv2;

	@ApiModelProperty(value="银行预留手机号码")
    @FieldEncrypt
    private String p13_phone;

	@ApiModelProperty(value="是否同步下发短信",hidden=true)
    @SignExclude
    private String sendValidateCode;

	@ApiModelProperty(value="签约类型",hidden=true)
    @SignExclude
    private String protocolType;

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

	public String getP4_orderId() {
		return p4_orderId;
	}

	public void setP4_orderId(String p4_orderId) {
		this.p4_orderId = p4_orderId;
	}

	public String getP5_timestamp() {
		return p5_timestamp;
	}

	public void setP5_timestamp(String p5_timestamp) {
		this.p5_timestamp = p5_timestamp;
	}

	public String getP6_payerName() {
		return p6_payerName;
	}

	public void setP6_payerName(String p6_payerName) {
		this.p6_payerName = p6_payerName;
	}

	public String getP7_idCardType() {
		return p7_idCardType;
	}

	public void setP7_idCardType(String p7_idCardType) {
		this.p7_idCardType = p7_idCardType;
	}

	public String getP8_idCardNo() {
		return p8_idCardNo;
	}

	public void setP8_idCardNo(String p8_idCardNo) {
		this.p8_idCardNo = p8_idCardNo;
	}

	public String getP9_cardNo() {
		return p9_cardNo;
	}

	public void setP9_cardNo(String p9_cardNo) {
		this.p9_cardNo = p9_cardNo;
	}

	public String getP10_year() {
		return p10_year;
	}

	public void setP10_year(String p10_year) {
		this.p10_year = p10_year;
	}

	public String getP11_month() {
		return p11_month;
	}

	public void setP11_month(String p11_month) {
		this.p11_month = p11_month;
	}

	public String getP12_cvv2() {
		return p12_cvv2;
	}

	public void setP12_cvv2(String p12_cvv2) {
		this.p12_cvv2 = p12_cvv2;
	}

	public String getP13_phone() {
		return p13_phone;
	}

	public void setP13_phone(String p13_phone) {
		this.p13_phone = p13_phone;
	}

	public String getSendValidateCode() {
		return sendValidateCode;
	}

	public void setSendValidateCode(String sendValidateCode) {
		this.sendValidateCode = sendValidateCode;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

    
}
