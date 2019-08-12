/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieCollectionPayUtil.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.util
 * Date:2018年9月10日下午6:00:11
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.creativearts.nyd.collectionPay.model.changjie.CardBinRequest;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJieConfig;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJiePublicQuickPayRequestConfig;

/**
 * ClassName:ChangJieCollectionPayUtil <br/>
 * Date:     2018年9月10日 下午6:00:11 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class ChangJieCollectionPayUtil {

	private static SimpleDateFormat sdfymd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfhsm = new SimpleDateFormat("HHmmss");
	/**
	 * 
	 * builderCollectionPayPublicReqParms:(组装代收付请求公用参数). <br/>
	 * @author wangzhch
	 * @param request
	 * @return
	 * @since JDK 1.8
	 */
	private static Map<String, String> builderCollectionPayPublicReqParms(ChangJiePublicQuickPayRequestConfig request) {
		Map<String, String> map = new HashMap<>();
		map.put("Service", request.getService());
		map.put("Version", request.getVersion());
		map.put("PartnerId", request.getPartnerId());
		map.put("TradeDate", sdfymd.format(new Date()));
		map.put("TradeTime", sdfhsm.format(new Date()));
		map.put("InputCharset", request.getInputCharset());
		map.put("Memo", null);
		return map;
	}
	
	/**
	 * 
	 * getCardBinRequestParms:(卡BIN信息查询). <br/>
	 * @author wangzhch
	 * @param cardBinRequest
	 * @param changJieConfig 
	 * @param request
	 * @param changJieConfig
	 * @param request 
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> getCardBinRequestParms(CardBinRequest cardBinRequest, ChangJieConfig changJieConfig, ChangJiePublicQuickPayRequestConfig request) {
		Map<String, String> map = builderCollectionPayPublicReqParms(request);
		map.put("TransCode", changJieConfig.getCardbinTransCode());
		map.put("OutTradeNo", cardBinRequest.getOutTradeNo());
		map.put("AcctNo", ChangJieSupplyUtil.encrypt(cardBinRequest.getAcctNo(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		return map;
	}
}

