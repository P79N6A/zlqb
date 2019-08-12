/**
 * Project Name:nyd-cash-pay-model
 * File Name:CardBinRequest.java
 * Package Name:com.creativearts.nyd.collectionPay.model.changjie
 * Date:2018年9月10日下午5:24:03
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.collectionPay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:CardBinRequest <br/>
 * Date:     2018年9月10日 下午5:24:03 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class CardBinRequest extends ChangJieCollectionPayRequest implements Serializable {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.8
	 */
	private static final long serialVersionUID = 7209647952837506876L;

	/**交易码*/
	private String transCode;
	
	/**交易请求号*/
	private String outTradeNo;
	
	/**待查账号:银行卡号*/
	private String acctNo;
	
	/**银行名称英文缩写*/
	private String bankCode;
}

