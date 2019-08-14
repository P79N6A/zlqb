package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayPayResp implements Serializable {

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

	// 请求提交时间 YYYYMMDDHHMMSS
	private String submitTime;

}
