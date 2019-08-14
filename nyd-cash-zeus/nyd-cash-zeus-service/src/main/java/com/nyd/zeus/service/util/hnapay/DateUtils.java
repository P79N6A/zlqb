/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * www.hnapay.com
 */

package com.nyd.zeus.service.util.hnapay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public final static String DEFAULT_PATTERN = "yyyyMMddHHmmss";
	public final static String FILEDATE_PATTERM = "yyyyMMdd";
	public final static String DATE_DEFAULT_PATTERN = "yyyy-MM-dd";
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd' 'HH:mm:ss";

	/**
	 * 获取当前时间(格式:yyyyMMddHHmmss)
	 *
	 * @return
	 */
	public static String getCurrDate() {
		return getCurrDate(DEFAULT_PATTERN);
	}

	/**
	 * 日期格式化，默认格式为yyyyMMddHHmmss
	 *
	 * @param date
	 *            日期
	 * @return
	 */
	public static String format(Date date) {
		return DateUtils.format(date, DEFAULT_PATTERN);
	}

	/**
	 * 日期格式化
	 *
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 获取当前时间
	 *
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getCurrDate(String pattern) {
		long d = System.currentTimeMillis();
		Date date = new Date(d);

		return format(date, pattern);
	}

}
