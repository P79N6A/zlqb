/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月4日下午6:10:18
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:鉴权绑卡响应参数 <br/>
 * Date:     2018年9月4日 下午6:10:18 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthResponse extends ChangJieQuickPayPublicResponse implements Serializable{

	private static final long serialVersionUID = -2056190803063649713L;

	/**商户网站唯一订单号*/
	@JsonProperty("TrxId")
	private String trxId;
	
	/**畅捷流水号*/
	@JsonProperty("OrderTrxid")
	private String orderTrxid;
	
	/**跳转地址*/
	@JsonProperty("InstUrl")
	private String instUrl;

	/**鉴权状态:S成功   F失败    P处理中*/
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

