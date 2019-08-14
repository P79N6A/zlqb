/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.model.hnapay;

/**
 * com.hnapay.expdemo.constant Created by weiyajun on 2017-03-02 11:08
 */
public class ExpConstant {

	public static String EXP10 = "EXP10";// 预绑卡
	public static String EXP11 = "EXP11";// 绑卡确认
	public static String EXP04 = "EXP04";// 解绑
	public static String EXP12 = "EXP12";// 预支付
	public static String EXP13 = "EXP13";// 支付确认
	public static String EXP08 = "EXP08";// 查询支付
	public static String EXP09 = "EXP09";// 退款

	public static String SGP01 = "SGP01";// 单笔付款-SGP01
	public static String SGP02 = "SGP02";// 单笔付款查询-SGP02

	public static String VERSION_VALUE = "2.0";// 版本号
	public static String SIGNTYPE_VALUE = "1";// 签名类型 RSA
	public static String CHARSET_VALUE = "1";// 编码类型 UTF-8
	public static String MERUSERIP_VALUE = "127.0.0.1";// 请求ip

	// 新生签名使用的算法
	public static String ALGORITHM = "RSA";

	public static String VERSION = "version";
	public static String TRANCODE = "tranCode";
	public static String MERID = "merId";
	public static String MERORDERID = "merOrderId";
	public static String SUBMITTIME = "submitTime";
	public static String MSGCIPHERTEXT = "msgCiphertext";
	public static String SIGNTYPE = "signType";
	public static String SIGNVALUE = "signValue";
	public static String MERATTACH = "merAttach";
	public static String CHARSET = "charset";

	public static String TRANAMT = "tranAmt";
	public static String PAYTYPE = "payType";
	public static String CARDTYPE = "cardType";
	public static String BANKCODE = "bankCode";
	public static String CARDNO = "cardNo";
	public static String BIZPROTOCOLNO = "bizProtocolNo";
	public static String PAYPROTOCOLNO = "payProtocolNo";
	public static String FRONTURL = "frontUrl";
	public static String NOTIFYURL = "notifyUrl";
	public static String ORDEREXPIRETIME = "orderExpireTime";
	public static String MERUSERIP = "merUserIp";
	public static String RISKEXPAND = "riskExpand";
	public static String GOODSINFO = "goodsInfo";
	public static String SUBMERCHANTID = "subMerchantId";
	public static String HNAPAYORDERID = "hnapayOrderId";// 新生订单号
	public static String HOLDNAME = "holderName";// 持卡人姓名
	public static String CARDAVAILABLEDATE = "cardAvailableDate";// 信用卡有效期
	public static String CVV2 = "cvv2";// 信用卡CVV2
	public static String MOBILENO = "mobileNo";// 银行签约手机号
	public static String IDENTITYTYPE = "identityType";// 证件类型
	public static String IDENTITYCODE = "identityCode";// 证件号码
	public static String MERUSERID = "merUserId";// 商户用户ID
	public static String PAYFACTORS = "payFactors";// 支付要素
	public static String RESULTCODE = "resultCode";// 处理结果码
	public static String ERRORCODE = "errorCode";// 异常代码
	public static String ERRORMSG = "errorMsg";// 异常描述

	public static String ORGMERORDERID = "orgMerOrderId"; // 原商户支付订单号
	public static String ORGSUBMITTIME = "orgSubmitTime"; // 原订单支付下单请求时间
	public static String ORDERAMT = "orderAmt"; // 原订单金额
	public static String REFUNDORDERAMT = "refundOrderAmt";// 退款金额
	public static String SMSCODE = "smsCode";// 签约短信验证码
	
	public static String PAYMENT_TERMINAL_INFO = "paymentTerminalInfo"; //付款方终端信息
	public static String RECEIVER_TERMINAL_INFO = "receiverTerminalInfo"; //收款方终端信息
	public static String DEVICE_INFO = "deviceInfo"; //设备信息
	
	public static String CHECKDATE = "checkDate";// 对账日期
	public static String REFUNDAMT = "refundAmt";// 已退费金额
	public static String ORDERSTATUS = "orderStatus";// 原订单交易状态
	public static String ORDERFAILEDCODE = "orderFailedCode";// 原订单交易返回码
	public static String ORDERFAILEDMSG = "orderFailedMsg";// 原订单交易失败原因
	public static String SUCCESSTIME = "successTime";// 订单完成时间

}
