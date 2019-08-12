package com.nyd.zeus.model.xunlian.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class XunlianQueryPayResp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 商户请求流水号
	private String respDate;
	
	// 商户请求流水号 查询结果为T时返回，1-支付，2-退款
	private String type;
	
	
	// 商户请求流水号
	private String merOrderId;

	// 金额
	private String amount;
	
	//T：成功F：失败 成功标识订单在系统中存在，失败标识系统未收到该笔订单请求
	private String queryResult;



	// 商户请求流水号 8029 成功
	private String resultCode;

	// T-成功，F-失败，P-未明
	private String resultMsg;

	// T-成功，F-失败，P-未明
	private String retFlag;

	// 签约协议号
	private String protocolId;

	// zhanghao1
	private String account;
	// 讯联智付交易流水号 暂时不用
	private String serialNo;

	// 短信发送编号
	private String smsSendNo;

}
