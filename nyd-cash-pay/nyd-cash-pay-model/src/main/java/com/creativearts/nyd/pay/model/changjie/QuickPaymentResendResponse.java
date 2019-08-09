/**
 * Project Name:nyd-cash-pay-model
 * File Name:QuickPaymentResendResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午4:55:53
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:短信验证码重发接口:返回参数 <br/>
 * Date:     2018年9月7日 下午4:55:53 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QuickPaymentResendResponse extends ChangJieQuickPayPublicResponse implements Serializable {
	
	private static final long serialVersionUID = -92369005165946212L;

	/**商户网站唯一订单号*/
	@JsonProperty("TrxId")
	private String trxId;
	
	/**畅捷流水号*/
	@JsonProperty("OrderTrxid")
	private String orderTrxId;
	
	/**业务状态*/
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

