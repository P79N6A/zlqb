package com.nyd.zeus.model.helibao.vo.pay.req;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

/**
 * Created by heli50 on 2017/4/14.
 */
@ApiModel(description="绑卡支付预下单")
public class QuickPayBindPayPreOrderVo implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="交易类型",hidden=true)
    private String p1_bizType;

	@ApiModelProperty(value="商户编号",hidden=true)
    private String p2_customerNumber;

	@ApiModelProperty(value="绑卡ID")
    private String p3_bindId;

	@ApiModelProperty(value="用户ID")
    private String p4_userId;

	@ApiModelProperty(value="商户订单号",hidden=true)
    private String p5_orderId;

	@ApiModelProperty(value="时间戳",hidden=true)
    private String p6_timestamp;

	@ApiModelProperty(value="交易币种",hidden=true)
    private String p7_currency;

	@ApiModelProperty(value="交易金额")
    private String p8_orderAmount;

	@ApiModelProperty(value="商品名称",hidden=true)
    private String p9_goodsName;

	@ApiModelProperty(value="商品描述",hidden=true)
    private String p10_goodsDesc;

	@ApiModelProperty(value="终端类型",hidden=true)
    private String p11_terminalType;

	@ApiModelProperty(value="终端标识")
    private String p12_terminalId;

	@ApiModelProperty(value="下单IP",hidden=true)
    private String p13_orderIp;

	@ApiModelProperty(value="订单有效时间",hidden=true)
    private String p14_period;

	@ApiModelProperty(value="订单有效时间单位",hidden=true)
    private String p15_periodUnit;

	@ApiModelProperty(value="服务器通知回调地址",hidden=true)
    private String p16_serverCallbackUrl;

	@ApiModelProperty(value="是否同步下发短信",hidden=true)
    @SignExclude
    private String sendValidateCode;

	@ApiModelProperty(value="商品数量",hidden=true)
    @SignExclude
    private String goodsQuantity;

	@ApiModelProperty(value="用户注册账号")
    @SignExclude
    private String userAccount;

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

	public String getP3_bindId() {
		return p3_bindId;
	}

	public void setP3_bindId(String p3_bindId) {
		this.p3_bindId = p3_bindId;
	}

	public String getP4_userId() {
		return p4_userId;
	}

	public void setP4_userId(String p4_userId) {
		this.p4_userId = p4_userId;
	}

	public String getP5_orderId() {
		return p5_orderId;
	}

	public void setP5_orderId(String p5_orderId) {
		this.p5_orderId = p5_orderId;
	}

	public String getP6_timestamp() {
		return p6_timestamp;
	}

	public void setP6_timestamp(String p6_timestamp) {
		this.p6_timestamp = p6_timestamp;
	}

	public String getP7_currency() {
		return p7_currency;
	}

	public void setP7_currency(String p7_currency) {
		this.p7_currency = p7_currency;
	}

	public String getP8_orderAmount() {
		return p8_orderAmount;
	}

	public void setP8_orderAmount(String p8_orderAmount) {
		this.p8_orderAmount = p8_orderAmount;
	}

	public String getP9_goodsName() {
		return p9_goodsName;
	}

	public void setP9_goodsName(String p9_goodsName) {
		this.p9_goodsName = p9_goodsName;
	}

	public String getP10_goodsDesc() {
		return p10_goodsDesc;
	}

	public void setP10_goodsDesc(String p10_goodsDesc) {
		this.p10_goodsDesc = p10_goodsDesc;
	}

	public String getP11_terminalType() {
		return p11_terminalType;
	}

	public void setP11_terminalType(String p11_terminalType) {
		this.p11_terminalType = p11_terminalType;
	}

	public String getP12_terminalId() {
		return p12_terminalId;
	}

	public void setP12_terminalId(String p12_terminalId) {
		this.p12_terminalId = p12_terminalId;
	}

	public String getP13_orderIp() {
		return p13_orderIp;
	}

	public void setP13_orderIp(String p13_orderIp) {
		this.p13_orderIp = p13_orderIp;
	}

	public String getP14_period() {
		return p14_period;
	}

	public void setP14_period(String p14_period) {
		this.p14_period = p14_period;
	}

	public String getP15_periodUnit() {
		return p15_periodUnit;
	}

	public void setP15_periodUnit(String p15_periodUnit) {
		this.p15_periodUnit = p15_periodUnit;
	}

	public String getP16_serverCallbackUrl() {
		return p16_serverCallbackUrl;
	}

	public void setP16_serverCallbackUrl(String p16_serverCallbackUrl) {
		this.p16_serverCallbackUrl = p16_serverCallbackUrl;
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
