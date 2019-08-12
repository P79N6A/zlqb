package com.zhiwang.zfm.common.util;

import java.util.Random;


/**
 * 
 * Classname  生成随机数	验证码 或者手机验证码
 * Version	  1.2
 * @author panye
 * 2014-8-5
 * Copyright notice
 */
public class Getrandom {

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
		if (length < 1) {
			return null;
		}
		if (randGen1 == null) {
			randGen1 = new Random();
			numbersAndLetters1 = ("0123456789abcdefghijklmnopqrstuvwxyz"
					+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		}
		char[] randBuffer1 = new char[length];
		for (int i = 0; i < randBuffer1.length; i++) {
			randBuffer1[i] = numbersAndLetters1[randGen1.nextInt(71)];
		}
		return new String(randBuffer1);
	}

	public synchronized static String createToke(){
		return new java.math.BigInteger(165, new Random()).toString(36).toUpperCase();
	}
	
	public static void main(String[] args) {
		for(int i =0 ;i<4;i++){
			
			System.out.println(randomString(3));
		}
	}
	
}
