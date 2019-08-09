/**
 * Project Name:nyd-cash-pay-model
 * File Name:QuickPaymentReceiptconfirmRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午7:32:41
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:QuickPaymentReceiptconfirmRequest <br/>
 * Date:     2018年9月7日 下午7:32:41 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QuickPaymentReceiptconfirmRequest implements Serializable {
	
	private static final long serialVersionUID = -2133991537133229245L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**原业务订单号*/
	private String orderTrxId;
	
	/**扩展字段*/
	private String extension;
}

