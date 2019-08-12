package com.nyd.zeus.model.xunlian.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class XunlianPayResp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 商户请求流水号
	private String payOrderId;

	// 金额
	private String amount;

	// 商户请求流水号
	private String respDate;

	// 商户请求流水号 8029 成功
	private String resultCode;

	// T-成功，F-失败，P-未明
	private String resultMsg;

	// T-成功，F-失败，P-未明
	private String retFlag;

	// 签约协议号
	private String protocolId;

	// name
	private String name;
	// zhanghao1
	private String account;
	// 讯联智付交易流水号 暂时不用
	private String serialNo;

	// 短信发送编号
	private String smsSendNo;

}
