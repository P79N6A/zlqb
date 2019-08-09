/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieConfig.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.properties
 * Date:2018年9月4日下午5:22:23
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ClassName:畅捷参数配置信息 <br/>
 * Date:     2018年9月4日 下午5:22:23 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Configuration
@Data
public class ChangJieConfig {

	@Value("${changjie.quickPaymentSmsconfirm}")
	private String quickPaymentSmsconfirm;//支付确认接口
	
	@Value("${changjie.directQuickPayment}")
	private String directQuickPayment;//支付请求接口(直接支付)
	
	@Value("${changjie.queryTrade}")
	private String queryTrade;//统一订单查询
	
	@Value("${changjie.changJiePublicKey}")
	private String changJiePublicKey;//畅捷支付平台公钥
	
	@Value("${changjie.zaoYiPrivateKey}")
	private String zaoYiPrivateKey;//造艺私钥
	
	@Value("${changjie.changJieReqUrl}")
	private String changJieReqUrl;//畅捷统一请求url
	
	@Value("${changjie.payNotifyUrl}")
	private String payNotifyUrl;//支付异步通知url
	
	@Value("${changjie.expiredTime}")
	private String ExpiredTime;//订单支付的有效时间
	
	@Value("${changjie.collectionpay.cardbin.transCode}")
	private String cardbinTransCode;//交易码
	
	@Value("${changjie.payOrder}")
	private String payOrder;//支付订单
}

