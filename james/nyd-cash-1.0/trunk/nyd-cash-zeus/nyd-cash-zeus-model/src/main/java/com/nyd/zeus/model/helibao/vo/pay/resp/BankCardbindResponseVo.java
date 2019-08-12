package com.nyd.zeus.model.helibao.vo.pay.resp;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.nyd.zeus.model.helibao.vo.pay.annotation.SignExclude;

@ApiModel(description="用户绑定银行卡信息查询返回报文")
public class BankCardbindResponseVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="交易类型")
	private String rt1_bizType;
	
	@ApiModelProperty(value="返回码")
	private String rt2_retCode;
	
	@ApiModelProperty(value="返回信息")
	private String rt3_retMsg;
	
	@ApiModelProperty(value="商户编号")
	private String rt4_customerNumber;
	
	@ApiModelProperty(value="交易卡信息列表")
	private String rt5_bindCardList;
	
	@ApiModelProperty(value="交易类型")
	@SignExclude
	private String sign;

	public String getRt1_bizType() {
		return rt1_bizType;
	}

	public void setRt1_bizType(String rt1_bizType) {
		this.rt1_bizType = rt1_bizType;
	}

	public String getRt2_retCode() {
		return rt2_retCode;
	}

	public void setRt2_retCode(String rt2_retCode) {
		this.rt2_retCode = rt2_retCode;
	}

	public String getRt3_retMsg() {
		return rt3_retMsg;
	}

	public void setRt3_retMsg(String rt3_retMsg) {
		this.rt3_retMsg = rt3_retMsg;
	}

	public String getRt4_customerNumber() {
		return rt4_customerNumber;
	}

	public void setRt4_customerNumber(String rt4_customerNumber) {
		this.rt4_customerNumber = rt4_customerNumber;
	}

	

	public String getRt5_bindCardList() {
		return rt5_bindCardList;
	}

	public void setRt5_bindCardList(String rt5_bindCardList) {
		this.rt5_bindCardList = rt5_bindCardList;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}
