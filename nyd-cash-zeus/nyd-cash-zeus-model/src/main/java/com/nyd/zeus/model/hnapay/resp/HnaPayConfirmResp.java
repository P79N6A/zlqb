package com.nyd.zeus.model.hnapay.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class HnaPayConfirmResp implements Serializable {

	private static final long serialVersionUID = 1L;

	// 业务协议号
	private String bizProtocolNo;

	// 支付协议号
	private String payProtocolNo;
	
	// 支付签约的银行简码
	private String bankCode;
}
