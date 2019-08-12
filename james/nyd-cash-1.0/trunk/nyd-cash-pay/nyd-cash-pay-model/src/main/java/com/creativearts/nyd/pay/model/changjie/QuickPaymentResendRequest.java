/**
 * Project Name:nyd-cash-pay-model
 * File Name:QuickPaymentResendRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午4:53:41
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:QuickPaymentResendRequest <br/>
 * Date:     2018年9月7日 下午4:53:41 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QuickPaymentResendRequest implements Serializable {
	
	private static final long serialVersionUID = 3654295936712748872L;
	
	/**商户网站唯一订单号*/
	private String trxId;
	
	/**原业务请求订单号*/
	private String oriTrxId;
	
	/**原业务订单类型*/
	private String tradeType;
	
	/**扩展字段*/
	private String extension;
}

