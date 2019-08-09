/**
 * Project Name:nyd-cash-pay-model
 * File Name:ChangJiePublicRequest.java
 * Package Name:com.creativearts.nyd.pay.model.changjie
 * Date:2018年9月4日下午5:00:48
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ClassName:畅捷支付请求公共报文头类 <br/>
 * Date:     2018年9月4日 下午5:00:48 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Configuration
@Data
public class ChangJiePublicQuickPayRequestConfig {
	/**接口名称*/
	@Value("${changjie.public.request.service}")
	private String service;
	
	/**接口版本*/
	@Value("${changjie.public.request.version}")
	private String version;

	/**合作者ID*/
	@Value("${changjie.public.request.partnerId}")
	private String partnerId;

	/**参数编码字符集*/
	@Value("${changjie.public.request.inputCharset}")
	private String inputCharset;

	/**请求的日期:年月日*/
	private String tradeDate;

	/**请求的时间:时分秒*/
	private String tradeTime;

	/**签名*/
	private String sign;

	/**签名方式*/
	private String signType;

	/**备注:可为空*/
	private String memo;
}

