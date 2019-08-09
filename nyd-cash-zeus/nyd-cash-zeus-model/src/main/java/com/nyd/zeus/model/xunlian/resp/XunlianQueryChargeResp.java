package com.nyd.zeus.model.xunlian.resp;

import java.io.Serializable;

import lombok.Data;

@Data
public class XunlianQueryChargeResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String respDate;

	private String type;
	
	private String payOrderId;
	
	//T：成功 F：失败
	private String queryResult;
	
	//账号
	private String account;
	
	//金额
	private String amount;
	
	//T:成功 F:失败 P:未明 结果
	private String retFlag;
	
	private String resultCode;
	
	private String resultMsg;
	



}
