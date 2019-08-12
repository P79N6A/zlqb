/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthUnBindCardRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午2:03:48
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:AuthUnBindCardRequest <br/>
 * Date:     2018年9月6日 下午2:03:48 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthUnBindCardRequest implements Serializable{

	private static final long serialVersionUID = -9128353936799175133L;
	
	/**商户网站唯一订单号*/
	private String trxId;
	
	/**商户编号:可空*/
	private String merchantNo;
	
	/**用户标识*/
	private String merUserId;
	
	/**解绑模式*/
	private String unbindType;
	
	/**卡号前6位*/
	private String cardBegin;
	
	/**卡号后4位*/
	private String cardEnd;
	
	/**扩展字段:可空*/
	private String extension;
}

