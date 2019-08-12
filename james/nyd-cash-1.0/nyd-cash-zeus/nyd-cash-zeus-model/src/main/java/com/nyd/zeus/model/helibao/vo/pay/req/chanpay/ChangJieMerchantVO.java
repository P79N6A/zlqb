package com.nyd.zeus.model.helibao.vo.pay.req.chanpay;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description="畅捷代付接口（转账给客户）")
public class ChangJieMerchantVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="银行名称")
	private String bankName; //银行名称
	
	@ApiModelProperty(value="用户账号")
	private String accntno; //用户账号
	
	@ApiModelProperty(value="账户名称")
	private String accntnm; //账户名称
	
	@ApiModelProperty(value="金额")
	private String amt; //   金额单位元
	
	@ApiModelProperty(value="手机号码")
	private String mobile; //手机号码
	
	@ApiModelProperty(value="渠道编码")
	private String code; //渠道编码
	
	@ApiModelProperty(value="流水号")
	private String serialNum; //流水号 

	@ApiModelProperty(value="回调地址")
	@Value("${common.changjiedf.callBack.url}")
	private String callBackUrl; //回调地址
	
	
	
	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccntno() {
		return accntno;
	}

	public void setAccntno(String accntno) {
		this.accntno = accntno;
	}

	public String getAccntnm() {
		return accntnm;
	}

	public void setAccntnm(String accntnm) {
		this.accntnm = accntnm;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
	
	
	
	
	
	
	

}
