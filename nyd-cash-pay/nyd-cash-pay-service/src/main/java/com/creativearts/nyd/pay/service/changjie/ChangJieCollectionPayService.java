/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieCollectionPayService.java
 * Package Name:com.creativearts.nyd.pay.service.changjie
 * Date:2018年9月10日下午5:49:05
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie;

import com.creativearts.nyd.collectionPay.model.changjie.CardBinRequest;
import com.tasfe.framework.support.model.ResponseData;

/**
 * ClassName:ChangJieCollectionPayService <br/>
 * Date:     2018年9月10日 下午5:49:05 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public interface ChangJieCollectionPayService {

	/**
	 * 
	 * getCardBin:(卡BIN信息查询). <br/>
	 * @author wangzhch
	 * @param cardBinRequest
	 * @return
	 * @since JDK 1.8
	 */
	ResponseData getCardBin(CardBinRequest cardBinRequest);

}

