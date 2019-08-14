package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayTransResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 0000 交易成功;4444 交易失败;5555 交易失效;9999 交易进行中
	private String resultCode;
	// 异常代码
	private String errorCode;
	// 异常描述
	private String errorMsg;

	// 商户请求流水号
	private String merOrderId;
	// 三方请求流水号
	private String hnapayOrderId;
	// 用户业务协议号，供二次支付使用
	private String bizProtocolNo;
	// 支付协议号，供二次支付使用
	private String payProtocolNo;
	// 对账日期
	private String checkDate;
	// 请求提交时间 YYYYMMDDHHMMSS
	private String submitTime;

}
