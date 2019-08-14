package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayRefundResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 0000 交易成功;4444 交易失败;5555 交易失效;9999 交易进行中
	private String resultCode;
	// 异常代码
	private String errorCode;
	// 异常描述
	private String errorMsg;

	// 三方请求流水号
	private String hnapayOrderId;
	// 原商户订单号
	private String orgMerOrderId;
	// 交易金额
	private String tranAmt;
	// 已退费金额
	private String refundAmt;
	// 原订单交易状态 0：交易已创建;1：交易成功;2：交易失效;3：交易失败;4：交易不存在
	private String orderStatus;
	
}
