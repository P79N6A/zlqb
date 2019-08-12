/**
 * Project Name:nyd-cash-pay-model
 * File Name:PaymentSmsConfirmRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午7:02:07
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:PaymentSmsConfirmRequest <br/>
 * Date:     2018年9月6日 下午7:02:07 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class PaymentSmsConfirmRequest implements Serializable {
	private static final long serialVersionUID = -239352948852398559L;

	/**商户网站唯一订单号*/
//	private String trxId;
	
	/**原支付请求订单号*/
	private String transId;
	
	/**确认支付短信验证码*/
	private String smsCode;
	
	/**扩展字段*/
	private String extension;
}

