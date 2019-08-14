package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayQueryPayResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 0000 交易成功;4444 交易失败;5555 交易失效;9999 交易进行中
	private String resultCode;
	// 异常代码
	private String errorCode;
	// 异常描述
	private String errorMsg;

	// 三方请求流水号
	private String hnapayOrderId;
	// 交易金额
	private String tranAmt;
	// 原订单交易状态 0：交易已创建;1：交易成功;2：交易失效;3：交易不存在
	private String orderStatus;
	// 原订单交易返回码
	private String orderFailedCode;
	// 原订单交易失败原因
	private String orderFailedMsg;
	// 订单完成时间 格式：YYYYMMDDHHmmss
	private String successTime;

}
