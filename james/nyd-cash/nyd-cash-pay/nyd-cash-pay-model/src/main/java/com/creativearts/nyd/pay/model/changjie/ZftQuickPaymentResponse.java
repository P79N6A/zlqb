/**
 * Project Name:nyd-cash-pay-model
 * File Name:ZftQuickPaymentResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午7:50:02
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:ZftQuickPaymentResponse <br/>
 * Date:     2018年9月6日 下午7:50:02 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */

/**
 * 请求畅捷返回的对象
 */
@Data
public class ZftQuickPaymentResponse extends ChangJieQuickPayPublicResponse implements Serializable {
	
	private static final long serialVersionUID = -1936763588867937980L;

	/**商户网站唯一订单号*/
	@JsonProperty("TrxId")
	private String trxId;
	
	/**畅捷流水号*/
	@JsonProperty("OrderTrxid")
	private String orderTrxId;
	
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
	
	/**扩展字段*/
	@JsonProperty("BindingCards")
	private String bindingCards;
}

