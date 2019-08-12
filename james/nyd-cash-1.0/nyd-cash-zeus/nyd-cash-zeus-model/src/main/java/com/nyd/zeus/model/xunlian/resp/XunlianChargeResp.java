package com.nyd.zeus.model.xunlian.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class XunlianChargeResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 商户请求流水号
	private String orderId;

	// 讯联智付交易流水号 暂时不用!!!!!
	private String serialNo;

	// 商户请求流水号
	private String respDate;

	// 账号
	private String account;

	// 金额
	private String amount;

	//返回code
	private String resultCode;

	//返回描述
	private String resultMsg;

	// T-成功，F-失败，P-未明
	private String retFlag;

}
