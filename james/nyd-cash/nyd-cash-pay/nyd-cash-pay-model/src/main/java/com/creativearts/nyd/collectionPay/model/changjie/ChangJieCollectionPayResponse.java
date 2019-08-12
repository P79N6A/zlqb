/**
 * Project Name:nyd-cash-pay-model
 * File Name:ChangJieCollectionPayResponse.java
 * Package Name:com.creativearts.nyd.collectionPay.model.changjie
 * Date:2018年9月11日上午10:12:38
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.collectionPay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:ChangJieCollectionPayResponse <br/>
 * Date:     2018年9月11日 上午10:12:38 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ChangJieCollectionPayResponse implements Serializable{
	/**受理状态*/
	@JsonProperty("AcceptStatus")
	private String acceptStatus;
	
	/**参数编码字符集*/
	@JsonProperty("InputCharset")
	private String inputCharset;
	
	/**平台受理返回代码*/
	@JsonProperty("PlatformRetCode")
	private String platformRetCode;
	
	/**创建时间戳*/
	@JsonProperty("TimeStamp")
	private String timeStamp;
	
	/**签名方式*/
	@JsonProperty("SignType")
	private String signType;
	
	/**签名*/
	@JsonProperty("Sign")
	private String sign;
	
	/**交易请求号*/
	@JsonProperty("TransCode")
	private String transCode;
	
	/**交易流水号*/
	@JsonProperty("OutTradeNo")
	private String outTradeNo;
	
	/**交易流水号*/
	@JsonProperty("FlowNo")
	private String flowNo;
	
	/**商户编号*/
	@JsonProperty("PartnerId")
	private String partnerId;
	
	/**受理状态返回信息*/
	@JsonProperty("RetMsg")
	private String retMsg;
	
	/**备注*/
	@JsonProperty("Memo")
	private String memo;
	
	/**平台受理错误描述*/
	@JsonProperty("PlatformErrorMessage")
	private String platformErrorMessage;
	
	/**返回码*/
	@JsonProperty("RetCode")
	private String retCode;
	
	/**请求的日期*/
	@JsonProperty("TradeDate")
	private String tradeDate;
	
	/**请求的时间*/
	@JsonProperty("TradeTime")
	private String tradeTime;
}

