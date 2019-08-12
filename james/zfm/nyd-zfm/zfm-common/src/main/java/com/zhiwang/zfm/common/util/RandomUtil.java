package com.zhiwang.zfm.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 
 * 功能说明：生成随机数 典型用法：用于生成验证码 特殊用法：该类在系统中的特殊用法的说明
 * 
 * @author panshangbin 修改人: 修改原因： 修改时间： 修改内容： 创建日期：2015-5-11 Copyright zzl-apt
 */
public class RandomUtil {

	/** * 产生随机字符串 * */
	private static Random randGen = null;
	private static Random randGen1 = null;
	private static char[] numbersAndLetters = null;
	private static char[] numbersAndLetters1 = null;

	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = ("0123456789").toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}

	public static final String randomString1(int length) {
		char[] randBuffer1 = new char[length];
		try {
			if (length < 1) {
				return null;
			}
			if (randGen1 == null) {
				randGen1 = new Random();
				numbersAndLetters1 = ("0123456789abcdefghijklmnopqrstuvwxyz"
						+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
			}

			for (int i = 0; i < randBuffer1.length; i++) {
				randBuffer1[i] = numbersAndLetters1[randGen1.nextInt(71)];
			}
		} catch (Exception e) {
			String result = UUID.randomUUID().toString();
			return result.length() > length ? result.substring(0, length)
					: null;
		}
		return new String(randBuffer1);
	}

	public synchronized static String createToke() {
		return new java.math.BigInteger(165, new Random()).toString(36)
				.toUpperCase();
	}


	public static String getOrderNumber() {

		int maxNum = 36;
		int i;
		int count = 0;
		char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while (count < 12) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return DateUtils.getCurrentTime(DateUtils.STYLE_3) + pwd.toString();
	}
	
}
