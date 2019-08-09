/**
 * Project Name:nyd-cash-pay-model
 * File Name:RefundTradeFileRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月7日下午7:02:02
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:RefundTradeFileRequest <br/>
 * Date:     2018年9月7日 下午7:02:02 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class RefundTradeFileRequest implements Serializable {
	
	private static final long serialVersionUID = -6534639850462002101L;

	/**交易日期:格式yyyyMMdd*/
	private String TransDate;
}

