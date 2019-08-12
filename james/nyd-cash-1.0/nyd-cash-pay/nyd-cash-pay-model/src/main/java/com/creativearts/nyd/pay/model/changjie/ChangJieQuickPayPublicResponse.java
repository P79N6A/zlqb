/**
 * Project Name:nyd-cash-pay-model
 * File Name:ChangJiePublicResponse.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月4日下午5:11:41
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * ClassName:畅捷支付响应公共报文头类 <br/>
 * Date:     2018年9月4日 下午5:11:41 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ChangJieQuickPayPublicResponse implements Serializable {

	private static final long serialVersionUID = -1259566626838971983L;
	/**畅捷分配给造艺商户号*/
	@JsonProperty("PartnerId")
	private String partnerId;
	
	/**参数编码字符集*/
	@JsonProperty("InputCharset")
	private String inputCharset;

	/**网关返回码*/
	@JsonProperty("AcceptStatus")
	private String acceptStatus;

	/**请求的日期*/
	@JsonProperty("TradeDate")
	private String tradeDate;

	/**请求的时间*/
	@JsonProperty("TradeTime")
	private String tradeTime;

	/**签名串*/
	@JsonProperty("Sign")
	private String sign;

	/**签名方式*/
	@JsonProperty("SignType")
	private String signType;

	/**备注*/
	@JsonProperty("Memo")
	private String memo;
}

