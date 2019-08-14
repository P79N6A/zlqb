/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import java.util.Random;

/**
 * com.hnapay.common.math Created by weiyajun on 2015/9/25 9:49
 */
public class HnapayRandom {

	/**
	 * 生成指定长度的十进制字符串
	 *
	 * @param hex_len
	 * @return 返回十进制的字符串
	 */

	public static String genRandomDec(int hex_len) {
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		final int maxNum = str.length;
		Random r = new Random();
		while (count < hex_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 生成指定长度的十六进制字符串
	 *
	 * @param hex_len
	 * @return 返回十进制的字符串
	 */

	public static String genRandomHex(int hex_len) {
		int i; // 生成的随机数
		int count = 0; // 生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4',
				'5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		final int maxNum = str.length;
		Random r = new Random();
		while (count < hex_len) {
			// 生成随机数，取绝对值，防止生成负数，
			i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

}
