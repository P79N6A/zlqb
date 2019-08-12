/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthInfoQryRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午3:29:48
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:AuthInfoQryRequest <br/>
 * Date:     2018年9月7日 下午3:29:48 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthInfoQryRequest implements Serializable {

	private static final long serialVersionUID = 2429851411631901745L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**用户标识*/
	private String merUserId;
	
	/**卡号前六位*/
	private String cardBegin;
	
	/**卡号后四位*/
	private String cardEnd;
	
	/**卡类型*/
	private String bkAcctTp;
	
	/**扩展字段*/
	private String extension;
}

