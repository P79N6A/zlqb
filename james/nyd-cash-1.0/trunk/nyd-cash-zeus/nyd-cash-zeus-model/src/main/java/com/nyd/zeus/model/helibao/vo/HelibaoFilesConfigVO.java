package com.nyd.zeus.model.helibao.vo;

import java.io.Serializable;

public class HelibaoFilesConfigVO implements Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;			//标识
	private String customerNumber;			//商户编号
	private String certPath;			//公钥路径
	private String pfxPath;			//私钥路径
	private String pfxPwd;			//
	private String payIp;			//服务ip
	private String payUrl;			//标准快捷url
	private String payCallbackUrl;			//支付回调地址
	private String entrustedLoanUrl;			//委托代付url
	private String entrustedUploanUrl;			//委托代付上传url
	private String entrustedCallbackUrl;			//代付回调地址
	private String signkey;			//MD5签名密钥
	private String signkeyPrivate;			//RSA私钥
	private String deskeyKey;			//3DES加密密钥
	private String goodsName;
	private String remark;			//备注
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	public String getCertPath() {
		return certPath;
	}
	public void setCertPath(String certPath) {
		this.certPath = certPath;
	}
	public String getPfxPath() {
		return pfxPath;
	}
	public void setPfxPath(String pfxPath) {
		this.pfxPath = pfxPath;
	}
	public String getPfxPwd() {
		return pfxPwd;
	}
	public void setPfxPwd(String pfxPwd) {
		this.pfxPwd = pfxPwd;
	}
	public String getPayIp() {
		return payIp;
	}
	public void setPayIp(String payIp) {
		this.payIp = payIp;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getPayCallbackUrl() {
		return payCallbackUrl;
	}
	public void setPayCallbackUrl(String payCallbackUrl) {
		this.payCallbackUrl = payCallbackUrl;
	}
	public String getEntrustedLoanUrl() {
		return entrustedLoanUrl;
	}
	public void setEntrustedLoanUrl(String entrustedLoanUrl) {
		this.entrustedLoanUrl = entrustedLoanUrl;
	}
	public String getEntrustedUploanUrl() {
		return entrustedUploanUrl;
	}
	public void setEntrustedUploanUrl(String entrustedUploanUrl) {
		this.entrustedUploanUrl = entrustedUploanUrl;
	}
	public String getEntrustedCallbackUrl() {
		return entrustedCallbackUrl;
	}
	public void setEntrustedCallbackUrl(String entrustedCallbackUrl) {
		this.entrustedCallbackUrl = entrustedCallbackUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSignkey() {
		return signkey;
	}
	public void setSignkey(String signkey) {
		this.signkey = signkey;
	}
	public String getSignkeyPrivate() {
		return signkeyPrivate;
	}
	public void setSignkeyPrivate(String signkeyPrivate) {
		this.signkeyPrivate = signkeyPrivate;
	}
	public String getDeskeyKey() {
		return deskeyKey;
	}
	public void setDeskeyKey(String deskeyKey) {
		this.deskeyKey = deskeyKey;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
		
	
}
