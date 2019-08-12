package com.nyd.zeus.model.helibao.vo.pay.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.FieldEncrypt;
import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

/**
 * Created by heli50 on 2017/4/14.
 */
@ApiModel(description="首次支付")
public class QuickPayFirstPayPreOrderVo implements Serializable{
	
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;

	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;

	@ApiModelProperty(value="用户id")
    private String p3_userId;

	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p4_orderId;

	@ApiModelProperty(value="时间戳-yyyyMMddHHmmss",hidden=true)
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

	@ApiModelProperty(value="交易币种",hidden=true)
    private String p14_currency;

	@ApiModelProperty(value="交易金额")
    private String p15_orderAmount;

	@ApiModelProperty(value="商品名称",hidden=true)
    private String p16_goodsName;

	@ApiModelProperty(value="商品描述",hidden=true)
    private String p17_goodsDesc;

	@ApiModelProperty(value="终端类型",hidden=true)
    private String p18_terminalType;

	@ApiModelProperty(value="终端标识")
    private String p19_terminalId;

	@ApiModelProperty(value="下单ip",hidden=true)
    private String p20_orderIp;

	@ApiModelProperty(value="订单有效时间",hidden=true)
    private String p21_period;

	@ApiModelProperty(value="有效时间单位",hidden=true)
    private String p22_periodUnit;

	@ApiModelProperty(value="服务器通知回调地址",hidden=true)
    private String p23_serverCallbackUrl;

	@ApiModelProperty(value="是否同步下发短信",hidden=true)
    @SignExclude
    private String sendValidateCode;
    
	@ApiModelProperty(value="商品数量",hidden=true)
    @SignExclude
    private String goodsQuantity;
    
	@ApiModelProperty(value="用户注册账号")
    @SignExclude
    private String userAccount;
    
	@ApiModelProperty(value="用户账号注册时间",hidden=true)
    @SignExclude
    private String enrollTime;
    
	@ApiModelProperty(value="交易定位地址",hidden=true)
    @SignExclude
    private String lbs;
    
	@ApiModelProperty(value="应用类型",hidden=true)
    @SignExclude
    private String appType;
    
	@ApiModelProperty(value="应用名",hidden=true)
    @SignExclude
    private String appName;
    
	@ApiModelProperty(value="业务场景",hidden=true)
    @SignExclude
    private String dealSceneType;
    
	@ApiModelProperty(value="场景参数",hidden=true)
    @SignExclude
    private String dealSceneParams;

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

	public String getP14_currency() {
		return p14_currency;
	}

	public void setP14_currency(String p14_currency) {
		this.p14_currency = p14_currency;
	}

	public String getP15_orderAmount() {
		return p15_orderAmount;
	}

	public void setP15_orderAmount(String p15_orderAmount) {
		this.p15_orderAmount = p15_orderAmount;
	}

	public String getP16_goodsName() {
		return p16_goodsName;
	}

	public void setP16_goodsName(String p16_goodsName) {
		this.p16_goodsName = p16_goodsName;
	}

	public String getP17_goodsDesc() {
		return p17_goodsDesc;
	}

	public void setP17_goodsDesc(String p17_goodsDesc) {
		this.p17_goodsDesc = p17_goodsDesc;
	}

	public String getP18_terminalType() {
		return p18_terminalType;
	}

	public void setP18_terminalType(String p18_terminalType) {
		this.p18_terminalType = p18_terminalType;
	}

	public String getP19_terminalId() {
		return p19_terminalId;
	}

	public void setP19_terminalId(String p19_terminalId) {
		this.p19_terminalId = p19_terminalId;
	}

	public String getP20_orderIp() {
		return p20_orderIp;
	}

	public void setP20_orderIp(String p20_orderIp) {
		this.p20_orderIp = p20_orderIp;
	}

	public String getP21_period() {
		return p21_period;
	}

	public void setP21_period(String p21_period) {
		this.p21_period = p21_period;
	}

	public String getP22_periodUnit() {
		return p22_periodUnit;
	}

	public void setP22_periodUnit(String p22_periodUnit) {
		this.p22_periodUnit = p22_periodUnit;
	}

	public String getP23_serverCallbackUrl() {
		return p23_serverCallbackUrl;
	}

	public void setP23_serverCallbackUrl(String p23_serverCallbackUrl) {
		this.p23_serverCallbackUrl = p23_serverCallbackUrl;
	}

	public String getSendValidateCode() {
		return sendValidateCode;
	}

	public void setSendValidateCode(String sendValidateCode) {
		this.sendValidateCode = sendValidateCode;
	}

	public String getGoodsQuantity() {
		return goodsQuantity;
	}

	public void setGoodsQuantity(String goodsQuantity) {
		this.goodsQuantity = goodsQuantity;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getEnrollTime() {
		return enrollTime;
	}

	public void setEnrollTime(String enrollTime) {
		this.enrollTime = enrollTime;
	}

	public String getLbs() {
		return lbs;
	}

	public void setLbs(String lbs) {
		this.lbs = lbs;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDealSceneType() {
		return dealSceneType;
	}

	public void setDealSceneType(String dealSceneType) {
		this.dealSceneType = dealSceneType;
	}

	public String getDealSceneParams() {
		return dealSceneParams;
	}

	public void setDealSceneParams(String dealSceneParams) {
		this.dealSceneParams = dealSceneParams;
	}

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

   
    
}
