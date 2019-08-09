/**
 * Project Name:nyd-cash-pay-model
 * File Name:ZftQuickPaymentRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月6日下午7:36:40
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * ClassName:ZftQuickPaymentRequest <br/>
 * Date:     2018年9月6日 下午7:36:40 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class ZftQuickPaymentRequest implements Serializable {
	
	private static final long serialVersionUID = -5701143365138068412L;

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
	
	/**卡类型*/
	private String bkAcctTp;
	
	/**卡号*/
	private String bkAcctNo;
	
	/**证件类型*/
	private String idTp;
	
	/**证件号*/
	private String idNo;
	
	/**持卡人姓名*/
	private String cstmrNm;
	
	/**银行预留手机号*/
	private String mobNo;
	
	/**cvv2码*/
	private String cardCvn2;
	
	/**有效期*/
	private String cardExprDt;
	
	/**担保金额*/
	private String ensureAmount;
	
	/**交易类型（即时 11 担保 12）*/
	private String tradeType;
	
	/**交易金额*/
	private BigDecimal trxAmt;
	
	/**交易金额分润账号集*/
	private String royaltyParameters;
	
	/**快捷版本*/
	private String payVersion;
	
	/**异步通知地址*/
	private String notifyUrl;
	
	/**扩展字段*/
	private String Extension;
}

