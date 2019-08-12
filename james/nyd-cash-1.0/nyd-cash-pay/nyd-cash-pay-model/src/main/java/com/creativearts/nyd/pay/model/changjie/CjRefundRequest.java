/**
 * Project Name:nyd-cash-pay-model
 * File Name:CjRefundRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午5:22:58
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:CjRefundRequest <br/>
 * Date:     2018年9月7日 下午5:22:58 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class CjRefundRequest implements Serializable {
	
	private static final long serialVersionUID = 5201306449443195398L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**原有支付请求订单号*/
	private String oriTrxId;
	
	/**退款金额*/
	private String trxAmt;
	
	/**退担保金额*/
	private String refundEnsureAmount;
	
	/**分润账户集*/
	private String royaltyParameters;
	
	/**异步通知地址*/
	private String notifyUrl;
	
	/**扩展字段*/
	private String extension;
}

