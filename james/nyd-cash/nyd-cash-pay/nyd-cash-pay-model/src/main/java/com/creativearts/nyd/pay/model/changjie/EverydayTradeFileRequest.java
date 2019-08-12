/**
 * Project Name:nyd-cash-pay-model
 * File Name:EverydayTradeFileRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月8日上午11:31:45
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:EverydayTradeFileRequest <br/>
 * Date:     2018年9月8日 上午11:31:45 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class EverydayTradeFileRequest implements Serializable {
	private static final long serialVersionUID = -9146986945574484191L;
	
	/**交易日期:yyyyMMdd*/
	private String transDate;
}

