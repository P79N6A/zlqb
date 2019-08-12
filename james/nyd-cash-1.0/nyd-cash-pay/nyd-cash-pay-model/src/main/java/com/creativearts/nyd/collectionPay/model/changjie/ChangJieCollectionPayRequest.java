/**
 * Project Name:nyd-cash-pay-model
 * File Name:ChangJieCollectionPayRequest.java
 * Package Name:com.creativearts.nyd.collectionPay.model.changjie
 * Date:2018年9月10日下午6:02:42
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.collectionPay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:ChangJieCollectionPayRequest <br/>
 * Date:     2018年9月10日 下午6:02:42 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ChangJieCollectionPayRequest implements Serializable {
	
	private static final long serialVersionUID = 6078678264016606268L;

	/**接口名称*/
	private String service;
	
	/**接口版本*/
	private String version;
	
	/**商户编号*/
	private String partnerId;
	
	/**请求的日期*/
	private String tradeDate;
	
	/**请求的时间*/
	private String tradeTime;
	
	/**参数编码字符集*/
	private String inputCharset;
	
	/**签名*/
	private String sign;
	
	/**签名方式*/
	private String signType;
	
	/**备注*/
	private String memo;
}

