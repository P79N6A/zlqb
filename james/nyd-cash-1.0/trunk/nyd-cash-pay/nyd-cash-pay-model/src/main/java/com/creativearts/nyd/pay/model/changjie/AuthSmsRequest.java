/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthSmsRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月5日下午2:00:25
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:鉴权绑卡确认请求参数 <br/>
 * Date:     2018年9月5日 下午2:00:25 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthSmsRequest implements Serializable{
	
	private static final long serialVersionUID = 1571906457683845280L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**原鉴权绑卡订单号*/
	private String oriAuthTrxid;
	
	/**鉴权短信验证码*/
	private String smsCode;
	
	/**扩展字段*/
	private String extension;
}

