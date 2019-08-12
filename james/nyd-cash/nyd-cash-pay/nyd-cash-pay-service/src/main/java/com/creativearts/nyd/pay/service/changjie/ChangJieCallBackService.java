/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieCallBackService.java
 * Package Name:com.creativearts.nyd.pay.service.changjie
 * Date:2018年9月12日下午7:44:35
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie;

import com.tasfe.framework.support.model.ResponseData;

/**
 * ClassName:ChangJieCallBackService <br/>
 * Date:     2018年9月12日 下午7:44:35 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ChangJieCallBackService {

	/**
	 * 
	 * checkSign:(这里用一句话描述这个方法的作用). <br/>
	 * @author wangzhch
	 * @param payCallBack
	 * @return
	 * @since JDK 1.8
	 */
	ResponseData checkPaySign(String payCallBack);

}

