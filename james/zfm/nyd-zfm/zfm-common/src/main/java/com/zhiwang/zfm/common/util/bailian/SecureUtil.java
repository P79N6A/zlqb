package com.zhiwang.zfm.common.util.bailian;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @Title SecureUtil.java
 * @Description 商户接入加解密工具包
 * @author linshao@qianbao.com
 * @date 2015年8月25日 下午2:06:17
 *
 */
public class SecureUtil {
	
	/**
	 * 解密交易信息.<br>
	 * 参数规则见接入文档.<br>
	 * 1.按照merchant+cer+data的顺序,中间不含任何拼接字符,拼接成字符串.用对方的公钥进行验签.<br>
	 * 2.用RSA的私钥解密证书cer,得到AES的秘钥.<br>
	 * 3.用AES秘钥解密data,得到明文交易JSON字符串.<br>
	 *
	 * @param merchant 商户号
	 * @param cer 证书
	 * @param data 数据集合
	 * @param sign 签名
	 * @param ourPrivateKey 己方的私钥
	 * @param otherPublicKey 对方的公钥
	 * @return 解密后的交易参数JSON格式明文
	 * 
	 * @throws Exception 签名异常,解密异常
	 */
	public static String decryptTradeInfo(String merchant,String cer,String data,String sign,String ourPrivateKey,String otherPublicKey) 
			throws Exception{
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(merchant);
		sBuffer.append(cer);
		sBuffer.append(data);
		String text = sBuffer.toString();
		String tradeJson = null;
		//1.按照merchant+cer+data的顺序,中间不含任何拼接字符,拼接成字符串.用对方的公钥进行验签
		if(!RSA.verify(sign, text , otherPublicKey, PayConstants.DEFAULT_CHARSET)){
			throw new SignatureException("验证签名不通过");
		}

		try {
			//2.用RSA的私钥解密证书cer,得到AES的秘钥
			String aesKey = RSA.decrypt(cer, ourPrivateKey, PayConstants.DEFAULT_CHARSET);
			//3.用AES秘钥解密data,得到明文交易JSON字符串
			tradeJson = AES.decrypt(data, aesKey, PayConstants.DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new SecurityException("解密异常");
		}
			
		return tradeJson;
	}
	
	/**
	 * 加密交易信息.<br>
	 * 参数规则见接入文档.<br>
	 * 1.随机生成一个AES的秘钥.<br>
	 * 2.用生成的AES秘钥加密交易信息JSON字符,得到数据集合密文data.<br>
	 * 3.将AES秘钥用对方的公钥加密,生成证书cer.<br>
	 * 4.按照merchant+cer+data的顺序,中间不含任何拼接字符,生成字符串,用自己的私钥签名.<br>
	 *
	 * @param merchant 商户号
	 * @param tradeJson JSON格式的交易明文信息
	 * @param ourPrivateKey 己方的私钥
	 * @param otherPublicKey 对方的公钥
	 * @return 返回key为MERCHANT,CER,DATA,SIGN的4个值的Map
	 * @throws Exception 签名异常,加密异常
	 */
	public static Map<String, String> encryptTradeInfo(String merchant,String tradeJson,String ourPrivateKey,String otherPublicKey) throws Exception{
		//1.随机生成一个AES的密钥
		String keySeed = UUID.randomUUID().toString();
		String aesKey = AES.key(keySeed, 128);
		
		String data = null;
		String cer = null;
		try {
			//2.用生成的AES秘钥加密交易信息JSON字符,得到数据集合密文data
			data = AES.encrypt(tradeJson, aesKey, PayConstants.DEFAULT_CHARSET);
			//3.将AES秘钥用对方的公钥加密,生成证书cer
			cer = RSA.encrypt(aesKey, otherPublicKey, PayConstants.DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new SecurityException("解密异常");
		}
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(merchant);
		sBuffer.append(cer);
		sBuffer.append(data);
		String text = sBuffer.toString();
		String sign = null;
		try {
			//4.按照merchant+cer+data的顺序,中间不含任何拼接字符,生成字符串,用自己的私钥签名
			sign = RSA.sign(text, ourPrivateKey, PayConstants.DEFAULT_CHARSET);
		} catch (Exception e) {
			throw new SignatureException("签名异常");
		}
		
		Map<String, String> args = new HashMap<String, String>();
		args.put(PayConstants.MERCHANT, merchant);
		args.put(PayConstants.CER, cer);
		args.put(PayConstants.DATA, data);
		args.put(PayConstants.SIGN, sign);
		
		return args;
	}
	
}
