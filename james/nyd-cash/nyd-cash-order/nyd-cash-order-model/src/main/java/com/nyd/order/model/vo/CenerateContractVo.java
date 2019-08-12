package com.nyd.order.model.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;

/**
 * 产品订单信息
 */
@Data
public class CenerateContractVo implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@ApiModelProperty(value = "用户id")
	private String userId;
	@ApiModelProperty(value = "订单id")
	private String orderId;
	@ApiModelProperty(value = "用户身份证")
	private String idNumber;
	@ApiModelProperty(value = "用户手机号")
	private String mobile;
	@ApiModelProperty(value = "用户姓名")
	private String userName;
	@ApiModelProperty(value = "借款金额(本金)")
	private String loanMoney;
	@ApiModelProperty(value = "还款日")
	private String loanDay;
	@ApiModelProperty(value = "期数")
	private String loanRate;
	@ApiModelProperty(value = "日利率")
	private String loanInterst;
	@ApiModelProperty(value = "银行code")
	private String bankCode;
	@ApiModelProperty(value = "银行卡号")
	private String bankAccount;
	@ApiModelProperty(value = "借款用途")
	private String loanUse;  //借款用途
	@ApiModelProperty(value = "逾期罚息")
	private String loanRate2;  //逾期罚息
	@ApiModelProperty(value = "批核服务费")
	private String fee;//批核服务费
	@ApiModelProperty(value = "服务费")
	private String fee1;//服务费
	@ApiModelProperty(value = "服务费(大写 )")
	private String fee2;	//服务费
	@ApiModelProperty(value = "用户地址")
	private String address;
}
