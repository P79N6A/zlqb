/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import java.net.URI;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;

import com.nyd.zeus.model.helibao.vo.pay.req.chanpay.PayConfigFileVO;
import com.nyd.zeus.model.hnapay.ExpConstant;

/**
 * com.hnapay.expdemo.util Created by weiyajun on 2017-03-02 9:31
 */
public class ExpUtil {

	private static int readTimeOut = 30000;
	private static int connectTimeOut = 30000;

	private static HttpTransport httpTransport = new HttpTransport();

	private static HttpPost method = null;

	// 加密字段
	public static Map<String, String[]> encryptField = new HashMap<String, String[]>();
	// 签名字段
	public static Map<String, String[]> signField = new HashMap<String, String[]>();
	// 验签字段
	public static Map<String, String[]> verifyField = new HashMap<String, String[]>();
	// 提交到新生的字段
	public static Map<String, String[]> submitField = new HashMap<String, String[]>();

	static {
		System.out.println("connectTimeOut=" + connectTimeOut);
		method = new HttpPost();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(readTimeOut)
				.setConnectTimeout(connectTimeOut)
				.setConnectionRequestTimeout(connectTimeOut).build();
		method.setHeader(HTTP.CONTENT_ENCODING, "UTF-8");
		method.setHeader(HTTP.USER_AGENT, "Rich Powered/1.0");
		method.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");
		method.setConfig(requestConfig);

		// 以下为加密的字段
		encryptField.put("EXP04", new String[] { "bizProtocolNo",
				"payProtocolNo", "merUserIp" });
		encryptField.put("EXP08", new String[] {});
		encryptField.put("EXP09", new String[] { "orgMerOrderId",
				"orgSubmitTime", "orderAmt", "refundOrderAmt", "notifyUrl" });
		encryptField.put("EXP10", new String[] { "cardType", "bankCode",
				"cardNo", "holderName", "cardAvailableDate", "cvv2",
				"mobileNo", "identityType", "identityCode", "merUserId",
				"merUserIp" });
		encryptField.put("EXP11", new String[] { "hnapayOrderId", "smsCode",
				"merUserIp" });
		encryptField.put("EXP12", new String[] { "tranAmt", "payType",
				"cardType", "bankCode", "cardNo", "holderName",
				"cardAvailableDate", "cvv2", "mobileNo", "identityType",
				"identityCode", "bizProtocolNo", "payProtocolNo", "frontUrl",
				"notifyUrl", "orderExpireTime", "merUserId", "merUserIp",
				"riskExpand", "goodsInfo", "subMerchantId" });
		encryptField.put("EXP13", new String[] { "hnapayOrderId", "smsCode",
				"merUserIp", "paymentTerminalInfo", "receiverTerminalInfo",
				"deviceInfo" });

		// 以下为签名字段
		String[] geSignField = new String[] { "version", "tranCode", "merId",
				"merOrderId", "submitTime", "msgCiphertext" };
		signField.put("EXP04", geSignField);
		signField.put("EXP08", new String[] { "version", "tranCode", "merId",
				"merOrderId", "submitTime" });
		signField.put("EXP09", geSignField);
		signField.put("EXP10", geSignField);
		signField.put("EXP11", geSignField);
		signField.put("EXP12", geSignField);
		signField.put("EXP13", geSignField);

		// 以下为收到新生响应及后台通知时的验签字段
		verifyField.put("EXP04", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode" });
		verifyField.put("EXP08", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode", "hnapayOrderId", "tranAmt", "refundAmt",
				"orderStatus" });
		verifyField.put("EXP09", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode", "hnapayOrderId", "orgMerOrderId", "tranAmt",
				"refundAmt", "orderStatus" });
		verifyField.put("EXP10", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode", "hnapayOrderId" });
		verifyField.put("EXP11", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode", "bizProtocolNo", "payProtocolNo", "bankCode",
				"cardType", "shortCardNo" });
		verifyField.put("EXP12", new String[] { "version", "tranCode",
				"merOrderId", "merId", "charset", "signType", "resultCode",
				"errorCode", "hnapayOrderId", "submitTime" });
		verifyField
				.put("EXP13", new String[] { "version", "tranCode",
						"merOrderId", "merId", "charset", "signType",
						"resultCode", "errorCode", "hnapayOrderId",
						"bizProtocolNo", "payProtocolNo", "tranAmt",
						"checkDate", "bankCode", "cardType", "shortCardNo" });

		// 需要提交到新生的字段
		String[] expGeSubmitField = new String[] { "version", "tranCode",
				"merId", "merOrderId", "submitTime", "msgCiphertext",
				"signType", "signValue", "merAttach", "charset" };
		String[] exp08SubmitField = new String[] { "version", "tranCode",
				"merId", "merOrderId", "submitTime", "signType", "signValue",
				"merAttach", "charset" };
		submitField.put("EXP04", expGeSubmitField);
		submitField.put("EXP08", exp08SubmitField);
		submitField.put("EXP09", expGeSubmitField);
		submitField.put("EXP10", expGeSubmitField);
		submitField.put("EXP11", expGeSubmitField);
		submitField.put("EXP12", expGeSubmitField);
		submitField.put("EXP13", expGeSubmitField);

	}

	/**
	 * 签名，提交到新生时需要做签名
	 *
	 * @param tranCode
	 *            交易码 例如 EXP01
	 * @param params
	 *            签名字段
	 * @return 签名值
	 */
	public static String sign(String tranCode, Map<String, String> params,
			PayConfigFileVO config) throws IllegalArgumentException {
		if (null == tranCode || "".equals(tranCode))
			throw new IllegalArgumentException("参数无效！");
		if (null == params)
			throw new IllegalArgumentException("参数无效！");
		String base64 = "";
		try {
			PrivateKey prikey = getPrivateKeyByPem(config.getPrdKey());
			String merData = genSingData(tranCode, params);
			byte[] b = RSAAlgorithms.getSignByte(prikey, merData);
			base64 = Base64Util.encode(b);
			base64 = base64.replace("\n", "").replace("\r", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base64;
	}

	/**
	 * 加密 ，对敏感信息的加密 ，签名前需要对敏感信息加密
	 *
	 * @param tranCode
	 *            交易码 例如 EXP01
	 * @param params
	 *            加密字段
	 * @return 密文
	 */
	public static String encrpt(String tranCode, Map<String, String> params,
			PayConfigFileVO config) throws IllegalArgumentException {
		if (null == tranCode || "".equals(tranCode))
			throw new IllegalArgumentException("参数无效！");
		if (null == params)
			throw new IllegalArgumentException("参数无效！");
		String plainData = genEncryptJson(tranCode, params);
		try {
			// 使用新生公钥加密 RSA算法
			String hexPublicKey = HexStringByte.byteToHex(Base64Util
					.decode(config.getPubKey()));
			byte[] cipherByte = RSAAlgorithms.encryptByPublicKey(
					plainData.getBytes("UTF-8"), hexPublicKey);
			String base64 = Base64Util.encode(cipherByte);
			base64 = base64.replace("\n", "").replace("\r", "");
			return base64;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 验证新生响应及后台通知的签名
	 *
	 * @param tranCode
	 *            交易码
	 * @param params
	 *            参数
	 * @return true验签通过 false 验签未通过或失败
	 * @throws Exception
	 */
	public static boolean verify(String tranCode, Map<String, Object> params,
			PayConfigFileVO config) throws Exception {
		if (null == tranCode || "".equals(tranCode))
			throw new IllegalArgumentException("参数无效！");
		if (null == params)
			throw new IllegalArgumentException("参数无效！");
		String signVal = params.get(ExpConstant.SIGNVALUE).toString();
		if (null == signVal || "".equals(signVal)) {
			throw new IllegalArgumentException("签名值不能为空！");
		}
		if (null != params.get(ExpConstant.PAYFACTORS)) {
			// 验签时支付要素要进行特殊处理
			ArrayList<String> list = (ArrayList<String>) params
					.get(ExpConstant.PAYFACTORS);
			StringBuffer factor = new StringBuffer();
			for (String str : list) {
				factor.append(str).append(",");
			}
			if (factor.length() > 0) {
				params.put(ExpConstant.PAYFACTORS,
						factor.substring(0, factor.length() - 1));
			} else {
				params.put(ExpConstant.PAYFACTORS, "");
			}
		}

		String verifyData = genVerifyData(tranCode, params);
		return RSAAlgorithms.verify(getPublicKeyByPem(config.getPubKey()),
				verifyData, Base64Util.decode(signVal));
	}

	/**
	 * 生成加密的JSON串
	 *
	 * @param tranCode
	 * @param params
	 * @return
	 */
	public static String genEncryptJson(String tranCode,
			Map<String, String> params) {
		String[] field = encryptField.get(tranCode.toUpperCase());
		if (null != field && field.length > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			for (int i = 0; i < field.length; i++) {
				sb.append("\"");
				sb.append(field[i]);
				sb.append("\":\"");
				sb.append(params.get(field[i]));
				sb.append("\"");
				if (i < field.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}");
			return sb.toString();
		} else {
			return "";
		}

	}

	/**
	 * 生成签名明文串
	 *
	 * @param tranCode
	 *            交易码
	 * @param params
	 *            参数
	 * @return 返回签名明文串
	 */
	private static String genSingData(String tranCode,
			Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		for (int i = 0; i < signField.get(tranCode.toUpperCase()).length; i++) {
			sb.append(signField.get(tranCode.toUpperCase())[i]);
			sb.append("=[");
			sb.append(params.get(signField.get(tranCode.toUpperCase())[i]));
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * 生成验签的明文
	 *
	 * @param tranCode
	 * @param params
	 * @return
	 */
	private static String genVerifyData(String tranCode,
			Map<String, Object> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		for (int i = 0; i < verifyField.get(tranCode.toUpperCase()).length; i++) {
			sb.append(verifyField.get(tranCode.toUpperCase())[i]);
			sb.append("=[");
			sb.append(params.get(verifyField.get(tranCode.toUpperCase())[i]));
			sb.append("]");
		}
		return sb.toString();
	}

	/**
	 * POST提交
	 *
	 * @param url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String submit(String tranCode, String url,
			Map<String, String> obj, PayConfigFileVO config) throws Exception {
		System.out.println("Submit Data:\n" + JsonUtil.MapToJson(obj));
		if (null == tranCode || "".equals(tranCode))
			throw new IllegalArgumentException("参数无效！");
		if (null == url || "".equals(url))
			throw new IllegalArgumentException("参数无效！");
		if (null == obj)
			throw new IllegalArgumentException("参数无效！");
		method.setURI(new URI(config.getPayUrl() + url + "?v="
				+ UUID.randomUUID()));
		httpTransport.setMethod(method);
		String response;
		Map<String, String> paraMap = new HashMap<String, String>();

		String[] field = submitField.get(tranCode.toUpperCase());
		for (int i = 0; i < field.length; i++) {
			paraMap.put(field[i], obj.get(field[i]));
		}
		if (url.startsWith("https")) {
			response = httpTransport.submit_https(paraMap);
		} else {
			response = httpTransport.submit(paraMap);
		}
		return response;
	}

	/**
	 * pem转私钥
	 *
	 * @param pem
	 * @return 返回私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKeyByPem(String pem) throws Exception {
		byte[] bPriKey = Base64Util.decode(pem);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bPriKey);
		KeyFactory keyFactory = KeyFactory.getInstance(ExpConstant.ALGORITHM);
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		return key;
	}

	/**
	 * pem转公钥
	 *
	 * @param pem
	 * @return 返回公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKeyByPem(String pem) throws Exception {
		return RSAAlgorithms.getPublicKey(Base64Util.decode(pem));
	}
}
