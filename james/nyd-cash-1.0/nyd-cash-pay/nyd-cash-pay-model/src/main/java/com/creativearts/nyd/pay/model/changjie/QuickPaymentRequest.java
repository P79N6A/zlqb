/**
 * Project Name:nyd-cash-pay-model
 * File Name:QuickPaymentRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午3:31:00
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:QuickPaymentRequest <br/>
 * Date:     2018年9月6日 下午3:31:00 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class QuickPaymentRequest implements Serializable {

	private static final long serialVersionUID = -7976412875600425855L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**商品名称*/
	private String ordrName;
	
	/**商品详情*/
	private String ordrDesc;
	
	/**用户标识*/
	private String merUserId;
	
	/**商户编号*/
	private String sellerId;
	
	/**子商户*/
	private String subMerchantNo;
	
	/**订单有效期*/
	private String expiredTime;
	
	/**担保金额*/
	private String ensureAmount;
	
	/**交易类型（即时 11 担保 12)*/
	private String tradeType;
	
	/**卡号前6位*/
	private String cardBegin;
	
	/**卡号后4位*/
	private String cardEnd;
	
	/**交易金额*/
	private String trxAmt;
	
	/**交易金额分润账号集*/
	private String royaltyParameters;
	
	/**异步通知地址*/
	private String notifyUrl;
	
	/**短信发送标识 0：不发送短信1：发送短信*/
	private String smsFlag;
	
	/**快捷版本  0标准版，1升级版*/
	private String payVersion;
	
	/**扩展字段*/
	private String extension;
}

