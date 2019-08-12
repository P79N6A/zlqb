package com.nyd.zeus.model.helibao.util.chanpay;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * <p>
 * 定义请求的参数名称
 * </p>
 * 
 * @author yanghta@chenjet.com
 * @version $Id: BaseConstant.java, v 0.1 2017-05-03 下午5:25:44
 */
public class BaseConstant {

	// 基础参数
	public static final String SERVICE = "Service";
	public static final String VERSION = "Version";
	public static final String PARTNER_ID = "PartnerId";
	// 日期
	public static final String TRADE_DATE = "TradeDate";
	public static final String TRADE_TIME = "TradeTime";
	public static final String INPUT_CHARSET = "InputCharset";
	public static final String SIGN = "Sign";
	public static final String SIGN_TYPE = "SignType";
	public static final String MEMO = "Memo";

	public static final String MD5 = "MD5";
	public static final String RSA = "RSA";
	
	/**
	 * 编码类型
	 */
	public static final String CHARSET = "UTF-8";
	public final static String GATEWAY_URL = "https://pay.chanpay.com/mag-unify/gateway/receiveOrder.do";
	public final static String BATCH_FILE_GATEWAY_URL = "https:/pay.chanpay.com/mag-unify/gateway/batchOrder.do";
	public static String DATE = new SimpleDateFormat("yyyyMMdd").format(new Date());
	public static String TIME = new SimpleDateFormat("HHmmss").format(new Date());

}
