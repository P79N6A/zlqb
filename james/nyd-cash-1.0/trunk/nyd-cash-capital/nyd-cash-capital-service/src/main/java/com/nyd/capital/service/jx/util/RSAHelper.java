package com.nyd.capital.service.jx.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


@Component
public class RSAHelper {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RSAHelper.class);
	private String encoding = "UTF-8";
	private String algorithm = "SHA1withRSA";// 加密算法  or "MD5withRSA"
	private PublicKey publicKey ;
	private PrivateKey privateKey ;
	private static RSAHelper instance;
	
	public RSAHelper(){
		//using default algorithm and encoding;
	}
	
	public RSAHelper(String algorithm) {
		this.algorithm=algorithm;
	}
	
	public RSAHelper(PublicKey publicKey) {
		this.publicKey=publicKey;
	}
	
	public RSAHelper(PrivateKey privateKey) {
		this.privateKey=privateKey;
	}

	public static RSAHelper getInstance(){
		if(instance==null){ instance = new RSAHelper();}
		return instance;
	}

	//please ensure map.remove("sign") is invoked first;
	public static String getValuesByKeySorted(Map<String,Object> map){
		Map<String,Object> sortedMap = map;
		if(!(map instanceof TreeMap)){
			sortedMap = new TreeMap();
			sortedMap.putAll(map);
		}
		StringBuilder sb = new StringBuilder();
		for(Entry<String,Object> ent:sortedMap.entrySet()){
			sb.append(ent.getValue()==null?"":ent.getValue().toString());
		}
		return sb.toString();
	}

	  /**
	  * 使用指定的公钥加密数据。
	  * 
	  * @param publicKey 给定的公钥。
	  * @param data 要加密的数据。
	  * @return 加密后的数据。
	  */
	 public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
	     //Cipher cipher = Cipher.getInstance(algorithm, DEFAULT_PROVIDER);
		    Cipher cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider()); 
		    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	     return cipher.doFinal(data);
	 }
	
	 /**
	  * 使用指定的私钥解密数据。
	  * @param privateKey 给定的私钥。
	  * @param data 要解密的数据。
	  * @return 原数据。
	  */
	 public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
	     //Cipher cipher = Cipher.getInstance(algorithm, DEFAULT_PROVIDER);
		    Cipher cipher= Cipher.getInstance("RSA");//, new BouncyCastleProvider()); 
	     cipher.init(Cipher.DECRYPT_MODE, privateKey);
	     return cipher.doFinal(data);
	 }

	/**
	 * 
	 * Description:校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false,要求全部参数不能为空
	 * 
	 * @return 校验成功返回true 失败返回false
	 */
	public boolean verify(String dataText, String signText) {
		try {
			byte[] signBytes = Base64.decodeBase64(signText);
			byte[] dataBytes = dataText.getBytes(encoding);
			return verify(dataBytes, signBytes);
		} catch (UnsupportedEncodingException e) {
			System.out.println("编码错误");
			e.printStackTrace();
		}
		// 取公钥匙对象
		return false;
	} 

	/**
	 * 验签 signature:加密串参数signature srcData：参数值的拼接串
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public boolean verify(byte[] srcData, byte[] signData) {
		try {
			Signature sig = Signature.getInstance(algorithm); 
			sig.initVerify(publicKey); // 初始化公钥用于验证的对象
			sig.update(srcData); // 验证数据
			return sig.verify(signData); // 验证传入的签名
		} catch (NoSuchAlgorithmException e) {
			System.out.println("初始化加密算法时报错");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("初始化公钥时报错");
			e.printStackTrace();
		} catch (SignatureException e) {
			System.out.println("验证数据时报错");
			e.printStackTrace();
		} catch (Throwable e) {
			System.out.println("校验签名失败");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param dataText 数据
	 * @return
	 */
	public String sign(String dataText){
		try {
			byte[] dataBytes = dataText.getBytes(encoding);
			byte[] signed = sign(dataBytes); 
			return Base64.encodeBase64String(signed);
		} catch (UnsupportedEncodingException e) {
			System.out.println("初始化加密算法时报错");
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * @param srcData 数据
	 * @return
	 */
	public byte[] sign(byte[] srcData) {
		try {
			Signature sig = Signature.getInstance(algorithm); 
			sig.initSign(privateKey);
			sig.update(srcData);
			byte[] signed = sig.sign(); // 对信息的数字签名
			return signed; 
		} catch (Exception e) {
			System.out.println("初始化加密算法时报错");
			e.printStackTrace();
		} catch (Throwable e) {
			System.out.println("校验签名失败");
			e.printStackTrace();
		}
		return null;
	}
}
