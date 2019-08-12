package com.zhiwang.zfm.common.util.bailian;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSA {
	public static final String ALGORITHM = "RSA";
	public static final String SIGN_ALGORITHM = "SHA1WithRSA";
	public static final String PUBLIC = "PUBLIC";
	public static final String PRIVATE = "PRIVATE";
	
	/**
	 * 公钥加密
	 * @param text
	 * @param key
	 * @param charset
	 * @return 密文Base64
	 * @throws Exception
	 */
	public static String encrypt(String text, String key, String charset) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(key);
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] textBytes = text.getBytes(charset);
		byte[] bytes = cipher.doFinal(textBytes);
		
		return new String(Base64.encodeBase64(bytes), charset);
	}
	
	/**
	 * 私钥解密
	 * @param text
	 * @param key
	 * @param charset
	 * @return 明文
	 * @throws Exception
	 */
	public static String decrypt(String text, String key, String charset) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	    
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] textBytes = Base64.decodeBase64(text);
		byte[] bytes = cipher.doFinal(textBytes);
	    
		return new String(bytes, charset);
	}
	
	/**
	 * 私钥签名
	 * @param text
	 * @param key
	 * @param charset
	 * @return 签名Base64
	 * @throws Exception
	 */
	public static String sign(String text, String key, String charset) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(key);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		Signature signature = Signature.getInstance(SIGN_ALGORITHM);
		signature.initSign(privateKey);
		signature.update(text.getBytes(charset));
		byte[] bytes = signature.sign();
		
		return new String(Base64.encodeBase64(bytes), charset);
	}
	
	/**
	 * 公钥验签
	 * @param text
	 * @param key
	 * @param charset
	 * @return 验签结果
	 * @throws Exception
	 */
	public static boolean verify(String sign, String text, String key, String charset) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(key);
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		
		Signature signature = Signature.getInstance(SIGN_ALGORITHM);
		signature.initVerify(publicKey);
		signature.update(text.getBytes(charset));
		boolean result = signature.verify(Base64.decodeBase64(sign.getBytes(charset)));
		
		return result;
	}
}