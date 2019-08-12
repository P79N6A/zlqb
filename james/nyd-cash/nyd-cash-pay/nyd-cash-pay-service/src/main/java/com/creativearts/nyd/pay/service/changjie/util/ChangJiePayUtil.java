/**
 * Project Name:nyd-cash-pay-service
 * File Name:ChangJieUtil.java
 * Package Name:com.creativearts.nyd.pay.service.changjie.util
 * Date:2018年9月4日下午7:24:54
 * Copyright (c) 2018, wangzhch All Rights Reserved.
 *
*/

package com.creativearts.nyd.pay.service.changjie.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.creativearts.nyd.pay.model.changjie.PaymentSmsConfirmRequest;
import com.creativearts.nyd.pay.model.changjie.ZftQuickPaymentRequest;
import com.creativearts.nyd.pay.service.changjie.enums.ChangJieEnum;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJieConfig;
import com.creativearts.nyd.pay.service.changjie.properties.ChangJiePublicQuickPayRequestConfig;
import com.nyd.pay.entity.ThirdPartyPaymentChannel;
/**
 * ClassName:ChangJieUtil <br/>
 * Date:     2018年9月4日 下午7:24:54 <br/>
 * @author   wangzhch
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
public class ChangJiePayUtil {
	
	private static SimpleDateFormat sdfymd = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdfhsm = new SimpleDateFormat("HHmmss");
	
	
	/**
	 * 
	 * builderPublicParms:(组装快捷支付公用参数). <br/>
	 * @author wangzhch
	 * @return
	 * @since JDK 1.8
	 */
	private static Map<String, String> builderPayPublicReqParms(ChangJiePublicQuickPayRequestConfig request) {
		Map<String, String> map = new HashMap<>();
		map.put("Version", request.getVersion());
		map.put("PartnerId", request.getPartnerId());
		map.put("InputCharset", request.getInputCharset());
		map.put("TradeDate", sdfymd.format(new Date()));
		map.put("TradeTime", sdfhsm.format(new Date()));
		map.put("Memo", null);
		return map;
	}
	
	/**
	 * 
	 * paymentSmsConfirmRequestParms:(支付确认接口). <br/>
	 * @author wangzhch
	 * @param paymentSmsConfirmRequest
	 * @param request
	 * @param changJieConfig
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> paymentSmsConfirmRequestParms(PaymentSmsConfirmRequest paymentSmsConfirmRequest,
			ChangJiePublicQuickPayRequestConfig request, ChangJieConfig changJieConfig) {
		Map<String, String> map = builderPayPublicReqParms(request);
		map.put("Service", changJieConfig.getQuickPaymentSmsconfirm());
		map.put("TrxId", paymentSmsConfirmRequest.getTransId());
		map.put("OriPayTrxId", paymentSmsConfirmRequest.getTransId());
		map.put("SmsCode", paymentSmsConfirmRequest.getSmsCode());
		map.put("Extension", null);
		return map;
	}

	/**
	 * 
	 * zftQuickPaymentRequestParms:(直接支付请求接口). <br/>
	 * @author wangzhch
	 * @param zftQuickPaymentRequest
	 * @param request
	 * @param changJieConfig
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> zftQuickPaymentRequestParms(ZftQuickPaymentRequest zftQuickPaymentRequest,
			ChangJiePublicQuickPayRequestConfig request, ChangJieConfig changJieConfig) {
		Map<String, String> map = builderPayPublicReqParms(request);
		map.put("Service", changJieConfig.getDirectQuickPayment());
		map.put("TrxId", zftQuickPaymentRequest.getTrxId());
		map.put("OrdrName", ChangJieEnum.ORDER_NAME.getMsg());
		map.put("MerUserId", zftQuickPaymentRequest.getMerUserId());
		map.put("SellerId", request.getPartnerId());
		map.put("SubMerchantNo", null);
		map.put("ExpiredTime", changJieConfig.getExpiredTime());
		map.put("BkAcctTp", zftQuickPaymentRequest.getBkAcctTp());
		map.put("BkAcctNo", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getBkAcctNo(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		map.put("IDTp", zftQuickPaymentRequest.getIdTp());
		map.put("IDNo", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getIdNo(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		map.put("CstmrNm", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getCstmrNm(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		map.put("MobNo", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getMobNo(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		if("00".equals(zftQuickPaymentRequest.getBkAcctTp())) {
			map.put("CardCvn2", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getCardCvn2(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
			map.put("CardExprDt", ChangJieSupplyUtil.encrypt(zftQuickPaymentRequest.getCardExprDt(),changJieConfig.getChangJiePublicKey(),request.getInputCharset()));
		}
		map.put("EnsureAmount", null);
		map.put("TradeType", "11");
		map.put("TrxAmt", String.valueOf(zftQuickPaymentRequest.getTrxAmt()));
		map.put("RoyaltyParameters", null);
		map.put("PayVersion", null);
		map.put("NotifyUrl", changJieConfig.getPayNotifyUrl());
		map.put("Extension", null);
		return map;
	}

	/**
	 * 
	 * queryTradeReceiptconfirmRequestParms:(向畅捷发起订单查询接口). <br/>
	 * @author wangzhch
	 * @param thirdPartyPaymentChannel
	 * @param request
	 * @param changJieConfig
	 * @return
	 * @since JDK 1.8
	 */
	public static Map<String, String> queryTradeReceiptconfirmRequestParms(ThirdPartyPaymentChannel thirdPartyPaymentChannel,
			ChangJiePublicQuickPayRequestConfig request, ChangJieConfig changJieConfig) {
		Map<String, String> map = builderPayPublicReqParms(request);
		map.put("Service", changJieConfig.getQueryTrade());
		map.put("TrxId", thirdPartyPaymentChannel.getTransId());
		map.put("OrderTrxId", thirdPartyPaymentChannel.getTransId());
		map.put("TradeType", changJieConfig.getPayOrder());//auth_order:鉴权订单,pay_order:支付订单,refund_order:退款订单
		map.put("Extension", null);
		return map;
	}

}

