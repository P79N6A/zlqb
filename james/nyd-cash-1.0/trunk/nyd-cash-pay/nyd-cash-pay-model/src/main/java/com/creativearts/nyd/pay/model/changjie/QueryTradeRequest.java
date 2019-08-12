/**
 * Project Name:nyd-cash-pay-model
 * File Name:QueryTradeRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月8日上午11:15:27
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:QueryTradeRequest <br/>
 * Date:     2018年9月8日 上午11:15:27 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QueryTradeRequest implements Serializable {
	private static final long serialVersionUID = -1403271114417831053L;
	
	/**商户网站唯一订单号*/
	private String trxId;
	
	/**原业务订单号*/
	private String orderTrxId;
	
	/**原业务订单类型*/
	private String tradeType;
	
	/**扩展字段*/
	private String extension;
}

