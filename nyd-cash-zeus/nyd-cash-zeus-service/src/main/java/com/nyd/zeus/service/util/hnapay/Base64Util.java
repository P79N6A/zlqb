/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import org.apache.commons.codec.binary.Base64;

/**
 * base64编码工具类
 *
 * @author wanggang
 * @version 2010-12-31
 */
@SuppressWarnings({ "all" })
public class Base64Util {

	/**
	 * 将 s 进行 BASE64 编码
	 *
	 * @param s
	 * @return
	 */
	public static String encode(byte[] s) {
		if (s == null)
			return null;
		return new Base64().encodeBase64String(s);
	}

	/**
	 * 将 s 进行 BASE64 编码
	 *
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		if (s == null)
			return null;
		return encode(s.getBytes());
	}

	/**
	 * 将 BASE64 编码的字符串 s 进行解码
	 *
	 * @param s
	 * @return
	 */
	public static byte[] decode(String s) {
		if (s == null)
			return null;
		Base64 decoder = new Base64();
		try {
			byte[] b = decoder.decodeBase64(s);
			return b;
		} catch (Exception e) {
			return null;
		}
	}

}
