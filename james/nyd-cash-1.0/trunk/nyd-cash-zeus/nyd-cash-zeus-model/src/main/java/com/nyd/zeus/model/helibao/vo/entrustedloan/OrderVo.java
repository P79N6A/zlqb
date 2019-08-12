package com.nyd.zeus.model.helibao.vo.entrustedloan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by heli50 on 2017/4/14.
 */

@ApiModel(description="委托代付下单")
public class OrderVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;
	
	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;
	
	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p3_orderId;
	
	@ApiModelProperty(value="用户编号")
    private String p4_userId;
	
	@ApiModelProperty(value="时间戳",hidden=true)
    private String p5_timestamp;
	
	@ApiModelProperty(value="币种",hidden=true)
    private String p6_currency;
	
	@ApiModelProperty(value="订单金额")
    private String p7_amount;
	
	@ApiModelProperty(value="账户类型")
    private String p8_business;
	
	@ApiModelProperty(value="账户名")
    private String p9_bankAccountName;
	
	@ApiModelProperty(value="账户号")
    private String p10_bankAccountNo;
	
	@ApiModelProperty(value="证件号")
    private String p11_legalPersonID;
	
	@ApiModelProperty(value="手机号")
    private String p12_mobile;
	
	@ApiModelProperty(value="借贷记类型",hidden=true)
    private String p13_onlineCardType;
	
	@ApiModelProperty(value="年份",hidden=true)
    private String p14_year;
	
	@ApiModelProperty(value="月份",hidden=true)
    private String p15_month;
	
	@ApiModelProperty(value="Cvv2",hidden=true)
    private String p16_cvv2;
	
	@ApiModelProperty(value="银行编码")
    private String p17_bankCode;
	
	@ApiModelProperty(value="联行号")
    private String p18_bankUnionCode;
	
	@ApiModelProperty(value="通知回调地址",hidden=true)
    private String p19_callbackUrl;
	
	@ApiModelProperty(value="用途")
    private String p20_purpose;
	
	@ApiModelProperty(value="借款信息 LoanConInfo")
    private String p21_loanConInfo;

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

	public String getP6_currency() {
		return p6_currency;
	}

	public void setP6_currency(String p6_currency) {
		this.p6_currency = p6_currency;
	}

	public String getP7_amount() {
		return p7_amount;
	}

	public void setP7_amount(String p7_amount) {
		this.p7_amount = p7_amount;
	}

	public String getP8_business() {
		return p8_business;
	}

	public void setP8_business(String p8_business) {
		this.p8_business = p8_business;
	}

	public String getP9_bankAccountName() {
		return p9_bankAccountName;
	}

	public void setP9_bankAccountName(String p9_bankAccountName) {
		this.p9_bankAccountName = p9_bankAccountName;
	}

	public String getP10_bankAccountNo() {
		return p10_bankAccountNo;
	}

	public void setP10_bankAccountNo(String p10_bankAccountNo) {
		this.p10_bankAccountNo = p10_bankAccountNo;
	}

	public String getP11_legalPersonID() {
		return p11_legalPersonID;
	}

	public void setP11_legalPersonID(String p11_legalPersonID) {
		this.p11_legalPersonID = p11_legalPersonID;
	}

	public String getP12_mobile() {
		return p12_mobile;
	}

	public void setP12_mobile(String p12_mobile) {
		this.p12_mobile = p12_mobile;
	}

	public String getP13_onlineCardType() {
		return p13_onlineCardType;
	}

	public void setP13_onlineCardType(String p13_onlineCardType) {
		this.p13_onlineCardType = p13_onlineCardType;
	}

	public String getP14_year() {
		return p14_year;
	}

	public void setP14_year(String p14_year) {
		this.p14_year = p14_year;
	}

	public String getP15_month() {
		return p15_month;
	}

	public void setP15_month(String p15_month) {
		this.p15_month = p15_month;
	}

	public String getP16_cvv2() {
		return p16_cvv2;
	}

	public void setP16_cvv2(String p16_cvv2) {
		this.p16_cvv2 = p16_cvv2;
	}

	public String getP17_bankCode() {
		return p17_bankCode;
	}

	public void setP17_bankCode(String p17_bankCode) {
		this.p17_bankCode = p17_bankCode;
	}

	public String getP18_bankUnionCode() {
		return p18_bankUnionCode;
	}

	public void setP18_bankUnionCode(String p18_bankUnionCode) {
		this.p18_bankUnionCode = p18_bankUnionCode;
	}

	public String getP19_callbackUrl() {
		return p19_callbackUrl;
	}

	public void setP19_callbackUrl(String p19_callbackUrl) {
		this.p19_callbackUrl = p19_callbackUrl;
	}

	public String getP20_purpose() {
		return p20_purpose;
	}

	public void setP20_purpose(String p20_purpose) {
		this.p20_purpose = p20_purpose;
	}

	public String getP21_loanConInfo() {
		return p21_loanConInfo;
	}

	public void setP21_loanConInfo(String p21_loanConInfo) {
		this.p21_loanConInfo = p21_loanConInfo;
	}

	

}
