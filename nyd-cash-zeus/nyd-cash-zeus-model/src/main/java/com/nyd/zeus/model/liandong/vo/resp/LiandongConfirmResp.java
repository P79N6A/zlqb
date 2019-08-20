package com.nyd.zeus.model.liandong.vo.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class LiandongConfirmResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 返回码
	private String ret_code;

	// 返回描述
	private String ret_msg;
	
	// 签约订单号
	private String bind_id;
	
	// 用户业务协议号 （当返回码为已签约时，会同时返回业务协议号和支付协议号。）
	// 当返回码为0000时，返回用户业务协议和支付协议号 
	private String usr_busi_agreement_id;
	
	// 用户业务协议号 （当返回码为已签约时，会同时返回业务协议号和支付协议号。备注：平台会为该用户的此银行卡注册支付协议，并生成此协议号。）
	//当返回码为0000时，返回用户业务协议和支付协议号 备注：平台会为该用户的此银行卡注册支付协议，并生成此协议号。
	private String usr_pay_agreement_id;
	
	
}
