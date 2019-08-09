/**
 * Project Name:nyd-cash-pay-model
 * File Name:QuickPaymentResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午3:45:38
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:QuickPaymentResponse <br/>
 * Date:     2018年9月6日 下午3:45:38 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QuickPaymentResponse extends ChangJieQuickPayPublicResponse implements Serializable {
	
	private static final long serialVersionUID = 5380284100189680662L;
	
	/**商户网站唯一订单号*/
	@JsonProperty("TrxId")
	private String trxId;
	
	/**畅捷流水号*/
	@JsonProperty("OrderTrxid")
	private String orderTrxid;
	
	/**订单状态*/
	@JsonProperty("Status")
	private String status;
	
	/**业务返回码*/
	@JsonProperty("RetCode")
	private String retCode;
	
	/**返回描述*/
	@JsonProperty("RetMsg")
	private String retMsg;
	
	/**应用返回码*/
	@JsonProperty("AppRetcode")
	private String appRetcode;
	
	/**应用返回描述*/
	@JsonProperty("AppRetMsg")
	private String appRetMsg;
	
	/**扩展字段*/
	@JsonProperty("Extension")
	private String extension;
}

