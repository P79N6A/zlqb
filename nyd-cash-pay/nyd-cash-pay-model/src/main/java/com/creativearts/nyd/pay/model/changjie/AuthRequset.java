/**
 * Project Name:nyd-cash-pay-model
 * File Name:AuthRequset.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月4日下午5:53:21
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.model.changjie;

import java.io.Serializable;

import lombok.Data;

/**
 * ClassName:鉴权绑卡请求参数 <br/>
 * Date:     2018年9月4日 下午5:53:21 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Data
public class AuthRequset implements Serializable {

	private static final long serialVersionUID = -6755969872048712881L;

	/**商户网站唯一订单号*/
	private String trxId;
	
	/**商户编号*/
	private String merchantNo;
	
	/**订单有效期*/
	private String expiredTime;
	
	/**用户标识*/
	private String merUserId;
	
	/**异步通知地址*/
	private String notifyUrl;
	
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
	
	/**短信发送标识*/
	private String smsFlag;
	
	/**快捷版本*/
	private String payVersion;
	
	/**扩展字段*/
	private String extension;
}

