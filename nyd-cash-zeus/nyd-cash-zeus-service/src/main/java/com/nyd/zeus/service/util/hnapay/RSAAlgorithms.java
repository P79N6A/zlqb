/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.management.openmbean.InvalidKeyException;

//import java.security.*;

/**
 * Created by weiyajun on 2015/9/23.
 */
public class RSAAlgorithms {

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	private static final String ALGORITHM = "RSA";

	/**
	 * 将十六进制 字符串转为公钥
	 * 
	 * @param hexStrKey
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String hexStrKey) throws Exception {
		PublicKey pubKey = null;
		try {
			byte[] encodedKey = HexStringByte.hex2byte(hexStrKey);
			pubKey = getPublicKey(encodedKey);
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥无效!", e);
		}
		return pubKey;
	}

	public static PublicKey getPublicKey(byte[] encodedKey) throws Exception {
		PublicKey pubKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
			pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(
					encodedKey));
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥无效!", e);
		}
		return pubKey;
	}

	/**
	 * 验证签名
	 *
	 * @param pubKey
	 *            公钥
	 * @param merData
	 *            签名数据
	 * @param signMsg
	 *            签名消息
	 * @return 返回验证结果 true 成功 false 失败
	 * @throws Exception
	 */
	public static Boolean verify(PublicKey pubKey, String merData,
			byte[] signMsg) throws Exception {

		boolean bVerify = false;
		Signature signet = Signature.getInstance("SHA1withRSA");
		try {
			signet.initVerify(pubKey);
		} catch (InvalidKeyException e) {
			throw new Exception("公钥无效!", e);
		}
		try {
			signet.update(merData.getBytes("UTF-8"));
		} catch (SignatureException e) {
			throw new Exception("验签时符号异常!", e);
		}

		try {
			bVerify = signet.verify(signMsg);
		} catch (SignatureException e) {
			throw new Exception("验签异常!", e);
		}
		return bVerify;
	}

	/**
	 * @param priKey
	 *            私钥
	 * @param data
	 *            要签名的数据
	 * @return 签名消息
	 * @throws Exception
	 */
	public static String sign(PrivateKey priKey, String data) throws Exception {
		try {
			Signature signet = Signature.getInstance("SHA1withRSA");
			signet.initSign(priKey);
			signet.update(data.getBytes("UTF-8"));
			return HexStringByte.byteToHex(signet.sign());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static byte[] getSignByte(PrivateKey priKey, String data)
			throws Exception {
		try {
			Signature signet = Signature.getInstance("SHA1withRSA");
			signet.initSign(priKey);
			signet.update(data.getBytes("UTF-8"));
			return signet.sign();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 使用私钥解密
	 *
	 * @param encryptStr
	 *            已加密串
	 * @param key
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptStr, PrivateKey key)
			throws Exception {
		if (encryptStr == null || "".equals(encryptStr)) {
			throw new Exception("密文串为空");
		}
		if (key == null) {
			throw new Exception("密钥为空");
		}
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
				key.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptStr.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptStr, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptStr, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * 使用私钥加密
	 *
	 * @param data
	 *            源数据
	 * @param key
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, PrivateKey key)
			throws Exception {
		if (data == null || "".equals(data)) {
			throw new Exception("需要加密的数据为空");
		}
		if (key == null) {
			throw new Exception("密钥为空");
		}
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
				key.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 *
	 * @param data
	 *            源数据
	 * @param publicKeyStr
	 *            公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKeyStr)
			throws Exception {
		if (data == null || "".equals(data)) {
			throw new Exception("需要加密的数据为空");
		}
		if (publicKeyStr == null || "".equals(publicKeyStr)) {
			throw new Exception("密钥为空");
		}

		PublicKey publicKey = getPublicKey(publicKeyStr);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 *
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKeyStr
	 *            公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData,
			String publicKeyStr) throws Exception {
		if (encryptedData == null || "".equals(encryptedData)) {
			throw new Exception("密文串为空");
		}
		if (publicKeyStr == null || "".equals(publicKeyStr)) {
			throw new Exception("密钥为空");
		}

		PublicKey publicKey = getPublicKey(publicKeyStr);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 *
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData,
			PublicKey publicKey) throws Exception {
		if (encryptedData == null || "".equals(encryptedData)) {
			throw new Exception("密文串为空");
		}
		if (publicKey == null) {
			throw new Exception("密钥为空");
		}

		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
				publicKey.getEncoded());
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}
}
