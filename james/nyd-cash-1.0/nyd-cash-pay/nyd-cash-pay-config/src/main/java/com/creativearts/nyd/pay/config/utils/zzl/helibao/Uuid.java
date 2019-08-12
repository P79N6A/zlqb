package com.creativearts.nyd.pay.config.utils.zzl.helibao;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author admin
 *
 */
public class Uuid {

	
	private final static String SYSTEM_ID="ZW";
	private static Object uuidLock = new Object();
	private static java.util.Random random = new java.util.Random(System
			.currentTimeMillis());

	private static long counter = (new java.util.Random(System
			.currentTimeMillis())).nextLong() & 0xFFFFF;
	
    /**
	 * 
	 * @return 返回长度为26位的字符串2位系统标识+YYYYMMDDHHMMSSZZZ+5位内部计数器+2位随机值 = 26位
	 */
	public static String getUuid26() {
		StringBuffer sb = new StringBuffer(26);
		sb.append(SYSTEM_ID);
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new java.util.Date()));
		String count = "00000" + next();
		sb.append(count.substring(count.length() - 5));

		count = "0" + random.nextLong();
		sb.append(count.substring(count.length() - 2));

		return sb.toString();
	}
	
	public static String getUuidYH26() {
		StringBuffer sb = new StringBuffer(26);
		sb.append("YH");
		sb.append(new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new java.util.Date()));
		String count = "00000" + next();
		sb.append(count.substring(count.length() - 5));

		count = "0" + random.nextLong();
		sb.append(count.substring(count.length() - 2));

		return sb.toString();
	}
	
	
	public static String getUuid12() {
		StringBuffer sb = new StringBuffer(16);
		sb.append(new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date()));
		String count1 = "00"+ next();
		sb.append(count1.substring(count1.length() - 2));
		String count2 = "00"+ random.nextLong();
		sb.append(count2.substring(count2.length() - 2));
		return sb.toString();
	}


	/**
	 * 
	 * @return 得到下一个内部计数值
	 */
	private static long next() {
		synchronized (uuidLock) {
			counter++;
			counter = counter & 0xFFFFF;

			return counter;
		}
	}



	public static void main(String[] args) {
		System.out.println(Uuid.getUuid26());
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		System.out.println(timestamp);
		
	
	}
}
