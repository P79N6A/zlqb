package com.zhiwang.zfm.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 日期处理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String STYLE_1 = "yyyy-MM-dd HH:mm:ss";

	public final static String STYLE_2 = "yyyy-MM-dd";

	public final static String STYLE_3 = "yyyyMMdd";

	public final static String STYLE_4 = "yyyyMMddhh";

	public final static String STYLE_5 = "yyyyMMddhhmm";

	public final static String STYLE_6 = "yyyy年MM月dd日HH时mm分ss秒";

	public final static String STYLE_7 = "yyyy年MM月dd日HH时mm分";

	public final static String STYLE_8 = "yyyy年MM月dd日";

	public final static String STYLE_9 = "hhmmss";

	public final static String STYLE_10 = "yyyyMMddhhmmss";
	

	/**
	 * format : MM-dd
	 */
	public final static String STYLE_11 = "MM-dd";
	/**
	 * format : HHmmss
	 */
	public final static String STYLE_12 = "HHmmss";
	

	/**
	 * format : yyyy-MM
	 */
	public final static String STYLE_13 = "yyyy-MM"; // 格式化时间为月份

	/**
	 * format : MM
	 */
	public final static String STYLE_14 = "MM";

	/**
	 * format : yyyyMMddhhmmss
	 */
	public final static String STYLE_15 = "yyyyMMddhhmmss";

	/**
	 * format : HH
	 */
	public final static String STYLE_16 = "HH";

	/**
	 * format : yyyyMMddhhmm
	 */
	public final static String STYLE_17 = "yyyy-MM-dd HH:mm";
	
	public final static String STYLE_18 = "ddhhmmss";

	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}
	

	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}
	
	public static String formatString(String date, String pattern) throws ParseException {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return format(df.parse(date),pattern);
		}
		return null;
	}
	

	/**
	 * 
	 * @desc String（时间）-string(时间)
	 * @date 2018年7月14日
	 * @auth zhenggang.Huang
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String formatString(String date) throws ParseException {
		return formatString(date, DATE_PATTERN);
	}
	
	/**
	 * 
	 *  功能说明：获得当前时间  panye  2014-11-29  @param style 时间类型 如果style 则默认返回yyyy-MM-dd
	 * HH:mm:ss  @return String 时间字符串     @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getCurrentTime(String style) {
		if (StringUtils.isEmpty(style)) {
			style = STYLE_1;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(style);
		String str = sdf.format(new Date());
		return str;
	}

	/**
	 * 
	 *  功能说明：获得当前时间  panye  2014-11-29  @param style 时间类型 如果style 则默认返回yyyy-MM-dd
	 * HH:mm:ss  @return String 时间字符串     @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getCurrentTime() {

		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_1);
		String str = sdf.format(new Date());
		return str;
	}
	

	/**
	 * 
	 *  功能说明：获得当前时间返回date类型    2018-07-12 
	 *  @param style 时间类型 如果style 则默认返回yyyy-MM-ddHH:mm:ss  @return String 时间字符串    
	 *  @throws  最后修改时间： 修改人： 修改内容： 修改注意点：
	 * @throws ParseException 
	 */
	public static Date getCurrentTimeDate() throws ParseException {
		DateFormat df = new SimpleDateFormat(STYLE_1);
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_1);
		String str = sdf.format(new Date());
		return df.parse(str);
	}

	/**
	 * 
	 *  功能说明：格式化日期  panye  2014-11-29  @param time 被格式化的日期 fmtStyle 格式化前的样式
	 * wantStyle 格式化后的样式  @return String 格式化后的日期  @throws  ParseException 最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static String formatDate(String time, String fmtStyle, String wantStyle) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmtStyle);
		Date date = sdf.parse(time);
		sdf = new SimpleDateFormat(wantStyle);

		return sdf.format(date);
	}

	/**
	 * 
	 *  功能说明：格式化日期  panye  2014-11-29  @param  times 预格式化的日期字符串 style 格式化后的样式 默认是
	 * yyyy-MM-dd HH:mm:ss  @return Date     @throws  ParseException 最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static Date formatDate(String times, String style) throws ParseException {
		if (style == null || "".equals(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		return sdf.parse(times);
	}

	/**
	 * 将String字符串转换为java.sql.Timestamp格式日期,用于数据库保存
	 * 
	 * @param strDate
	 *            表示日期的字符串
	 * @param dateFormat
	 *            传入字符串的日期表示格式（如："yyyy-MM-dd HH:mm:ss"）
	 * @return java.sql.Timestamp类型日期对象（如果转换失败则返回null）
	 */
	public static java.sql.Timestamp strToSqlDate(String strDate, String dateFormat) {
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		java.util.Date date = null;
		try {
			date = sf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Timestamp dateSQL = new java.sql.Timestamp(date.getTime());
		return dateSQL;
	}

	/**
	 * 
	 *  功能说明：格式化日期  panye  2014-11-29  @param time 被格式化的日期 fmtStyle 格式化前的样式
	 * wantStyle 格式化后的样式  @return String 格式化后的日期  @throws  ParseException 最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static String toFormatDate(String time, String fmtStyle, String wantStyle) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmtStyle);
		Date date = sdf.parse(time);
		sdf = new SimpleDateFormat(wantStyle);

		return sdf.format(date);
	}

	/**
	 * 
	 *  功能说明：根据生日求年龄 周岁  panye  2014-11-29  @param birthDay 生日  @return int 周岁  
	 *  @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static int getAgeByBirthDay(String birthDay) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_2);
		Date date = sdf.parse(birthDay);
		if (cal.before(date)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(date);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		} else {
			return 0;
		}

		return age;
	}

	/**
	 * 
	 *  功能说明：比较两个日期 大小  panye  2014-11-29  @param DATE1 日期1 DATE2 日期2  @return   返回
	 * int (-1 ：日期1 大于 日期2,0 ：日期1 小于日期2, 1： 日期1 等于日期2)  @throws  最后修改时间： 修改人：panye
	 * 修改内容： 修改注意点：
	 */
	public static int compareDate(String DATE1, String DATE2) {
		int i = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				i = -1;
			} else if (dt1.getTime() < dt2.getTime()) {
				i = 0;
			} else if (dt1.getTime() == dt2.getTime()) {
				i = 1;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return i;
	}


	/**
	 * 
	 *  功能说明：比较两个日期 大小  panye  2014-11-29  @param DATE1 日期1 DATE2 日期2  @return   返回
	 * int (-1 ：日期1 大于 日期2 , 0 ：日期1 小于 日期2 , 1 ：日期1 等于 日期2 )  @throws  最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static int compareDate(String DATE1, String DATE2, String style) {
		int i = 0;
		SimpleDateFormat df = new SimpleDateFormat(style);
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				i = -1;
			} else if (dt1.getTime() < dt2.getTime()) {
				i = 0;
			} else if (dt1.getTime() == dt2.getTime()) {
				i = 1;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return i;
	}

	/**
	 * 
	 *  功能说明：获得指定日期的最后一天  panye  2014-11-29  @param  date 指定日期  @return     @throws 
	 * 该方法可能抛出的异常，异常的类型、含义。 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 
	 *  功能说明：获得本月第一天日期  panye  2014-11-29  @param   @return   String 2015-05-01
	 *  @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getMonthBegin() {
		Calendar localTime = Calendar.getInstance();
		String strY = null;
		int x = localTime.get(Calendar.YEAR);
		int y = localTime.get(Calendar.MONTH) + 1;
		strY = y >= 10 ? String.valueOf(y) : ("0" + y);
		return x + "-" + strY + "-01";
	}

	/**
	 * 
	 *  功能说明：获得本月最后一天日期  panye  2014-11-29  @param   @return   String 2015-05-01
	 *  @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getMonthEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
		int endday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + endday;
	}

	/**
	 * 
	 *  功能说明：判断指定日期是否为周六  panye  2014-11-29  @param  date 日期字符串  @return   
	 *  @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static boolean isWeekOfSaturday(String date) throws ParseException {

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate = format1.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(bdate);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			return true;
		return false;
	}

	/**
	 * 
	 *  功能说明：判断指定日期是否为周日  panye  2014-11-29  @param  date 日期字符串  @return   
	 *  @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static boolean isWeekOfSunday(String bDate) throws ParseException {

		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate = format1.parse(bDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(bdate);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			return true;
		return false;
	}

	/**
	 * 
	 *  功能说明：求得指定日期的前N天日期  panye  2014-11-29  @param  date 日期 ，day 天数 ，style
	 * 预转换的日期格式 （默认为 yyyy-MM-dd HH:mm:ss）  @return  String    @throws 
	 * 该方法可能抛出的异常，异常的类型、含义。 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getBefore(Date date, int day, String style) {
		if (ChkUtil.isEmpty(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		sdf.format(date);
		return sdf.format(now.getTime());
	}
	
	/**
	 * 
	 *  功能说明：求得指定日期的后N天日期  panye  2014-11-29  @param  date 日期 ，day 天数 ，style
	 * 预转换的日期格式 （默认为 yyyy-MM-dd HH:mm:ss）  @return  String    @throws 
	 * 该方法可能抛出的异常，异常的类型、含义。 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getAfter(Date date, int day, String style) {
		if (ChkUtil.isEmpty(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		sdf.format(date);
		return sdf.format(now.getTime());
	}

	/**
	 * 
	 *  功能说明：是否为闰年  panye  2015-5-13  @param year 年份  @return   boolean  @throws 
	 * 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	/**
	 * 
	 *  功能说明：根据当前日期计算出后面几个月后的日期或者 几天后的日期  panye  2015-5-13  @param nowTime 当时日期
	 * type{2 代表月 其它代表 日} style 指定格式化日期样式 默认 yyyy-MM-dd HH:mm:ss  @return   
	 *  @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getLastTime(String nowTime, int type, int increment, String style) throws ParseException {
		if (style == null || "".equals(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(nowTime));
		if (type == 2)
			calendar.add(Calendar.MONTH, increment);
		else
			calendar.add(Calendar.DAY_OF_MONTH, increment);

		String sdate = sdf.format(calendar.getTime());
		calendar.setTime(sdf.parse(sdate));
		return sdate;
	}

	/**
	 * 
	 *  功能说明：求两个日期之间相隔天数  panye  2015-5-13  @param  time1 日期1 time2 日期2  @return
	 * long   相差天数  @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static long getBetweenDays(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat(STYLE_2);
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();

			quot = quot / 1000 / 60 / 60 / 24;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Math.abs(quot);
	}

	/**
	 * 
	 *  功能说明：求两个日期之间相隔天数  panye  2015-5-13  @param  time1 日期1 time2 日期2  @return
	 * long   相差天数  @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static long getBetweenHours(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat(STYLE_9);
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			System.out.println(quot / 1000 / 60 / 60);
			quot = quot / 1000 / 60;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Math.abs(quot);
	}

	/**
	 * 
	 *  功能说明：求两个日期相差的天数  panye  2015-5-13  @param   @return   天数（int）  @throws 
	 * ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static int diffDays(String startDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(startDate));
		long startTime = cal.getTimeInMillis();
		cal.setTime(sdf.parse(endDate));
		long endTime = cal.getTimeInMillis();
		long between_days = (endTime - startTime) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 
	 *  功能说明：求两个日期相差的秒/分/时  panye  2015-5-13  @param date1 日期1 date2 日期2 type（1 秒 2
	 * 分 3 时）  @return     @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static long getBetweenTimes(String date1, String date2, int type) throws ParseException {
		SimpleDateFormat s = new SimpleDateFormat(STYLE_1);
		long t1 = s.parse(date1).getTime();
		long t2 = s.parse(date2).getTime();
		long result = 0;
		switch (type) {
		// 秒
		case 1:
			result = (t2 - t1) / 1000;
			break;

		// 分
		case 2:
			result = (t2 - t1) / 1000 / 60;
			break;

		// 时
		case 3:
			result = (t2 - t1) / 1000 / 60 / 60;
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 
	 *  功能说明：比较两个日期  panye  2015-5-13  @param x天x小时x分  @return     @throws 
	 * 该方法可能抛出的异常，异常的类型、含义。 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 * 
	 * @throws ParseException
	 */
	public static String getDistanceTime(String str1, String str2) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long day = 0;
		long hour = 0;
		long min = 0;
		one = df.parse(str1);
		two = df.parse(str2);
		long time1 = one.getTime();
		long time2 = two.getTime();
		if (time2 <= time1)
			return 0 + "天" + 0 + "小时" + 0 + "分";

		long diff = time2 - time1;
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		return day + "天" + hour + "小时" + min + "分";
	}

	/**
	 * 
	 *  功能说明：得到当前时间所处的时间段  panye  2015-5-13  @param   @return   1 凌晨 2 早上 3中午 4下午 5
	 * 晚上  @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static int PeriodOfTime() {
		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int result = 0;
		// 早上
		if (hours >= 0 && hours <= 5) {
			// 凌晨
			result = 1;
		} else if (hours >= 6 && hours <= 10) {
			// 早上
			result = 2;
		} else if (hours >= 11 && hours <= 13) {
			// 中午
			result = 3;
		} else if (hours >= 14 && hours <= 18) {
			// 下午
			result = 4;
		} else if (hours >= 19 && hours <= 24) {
			// 晚上
			result = 5;
		}
		return result;
	}

	/**
	 * 
	 *  功能说明：获得当前年份  panye  2015-5-13  @param   @return   年份  @throws  最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 
	 *  功能说明：获得当前月份  panye  2015-5-13  @param   @return  月份  @throws  最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static int getCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	// 根据 年月日 获取 月份
	public static int getMonthByTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_2);
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(time));
		int month = cal.get(Calendar.MONTH) + 1;
		return month;
	}

	
	public static String getDatePoor(String nowDateS, String endDateS) {
		Date endDate = strToDate(endDateS, STYLE_1);
		Date nowDate = strToDate(nowDateS, STYLE_1);
		return getDatePoor(nowDate, endDate);
	}

	/**
	 * @desc 获取2个时间段的时间差(以小时为单位)
	 * @date 2018年6月5日
	 * @auth zhenggang.Huang
	 * @param inTime
	 * @param endtime
	 * @return
	 */
	public static String getDatePoor(Date inTime, Date endtime) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endtime.getTime() - inTime.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff / nh - (day * 24);
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		String timeString = "";

		if (day == 0) {
			if (hour == 0) {
				return min + "分钟";
			} else {
				return hour + "小时" + min + "分钟";
			}
		} else {
			return day + "天" + hour + "小时" + min + "分钟";
		}
	}

	/**
	 * 
	 *  功能说明：获得当前日份  panye  2015-5-13  @param   @return  日  @throws  最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static int getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DATE);
	}

	/**
	 * 
	 *  功能说明：该方法实现的功能  panye  2015-5-13  @param   @return  日  @throws  最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取本月的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate, String style) {
		if (ChkUtil.isEmpty(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 
	 *  功能说明：获得 当前时间的前几分钟时间  panye  2015-6-1  @param   @return     @throws 
	 * 该方法可能抛出的异常，异常的类型、含义。 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getBeforeSeconds(int second, String style) {
		Date date = new Date();
		SimpleDateFormat sdf = formatMap.get(style);
		if (sdf == null) {
			sdf = new SimpleDateFormat(style);
		}
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.SECOND, now.get(Calendar.SECOND) - second);
		sdf.format(date);
		return sdf.format(now.getTime());
	}

	// add by WuJing 20170221 性能优化
	private static Map<String, SimpleDateFormat> formatMap = new HashMap<String, SimpleDateFormat>();

	static {
		formatMap.put(STYLE_1, new SimpleDateFormat(STYLE_1));
		formatMap.put(STYLE_2, new SimpleDateFormat(STYLE_2));
		formatMap.put(STYLE_3, new SimpleDateFormat(STYLE_3));
		formatMap.put(STYLE_4, new SimpleDateFormat(STYLE_4));
		formatMap.put(STYLE_5, new SimpleDateFormat(STYLE_5));
		formatMap.put(STYLE_6, new SimpleDateFormat(STYLE_6));
		formatMap.put(STYLE_7, new SimpleDateFormat(STYLE_7));
		formatMap.put(STYLE_8, new SimpleDateFormat(STYLE_8));
		formatMap.put(STYLE_9, new SimpleDateFormat(STYLE_9));
		formatMap.put(STYLE_10, new SimpleDateFormat(STYLE_10));
		formatMap.put(STYLE_11, new SimpleDateFormat(STYLE_11));
		formatMap.put(STYLE_12, new SimpleDateFormat(STYLE_12));
		formatMap.put(STYLE_13, new SimpleDateFormat(STYLE_13));
		formatMap.put(STYLE_14, new SimpleDateFormat(STYLE_14));
		formatMap.put(STYLE_15, new SimpleDateFormat(STYLE_15));
		formatMap.put(STYLE_16, new SimpleDateFormat(STYLE_16));
		formatMap.put(STYLE_17, new SimpleDateFormat(STYLE_17));
	}
	
    /** 
     * @Description: 将时间戳转换为时间
     * @Author: taohui   
     * @param s
     * @param style
     * @return
     * @CreateDate: 2018年12月1日 上午10:57:55
     * @throws Exception 异常
     */
    public static String stampToDate(String s, String style){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
	/** 
	 * @Description: 求得指定日期的前N日的日期
	 * @Author: taohui   
	 * @param beforeDate  日期
	 * @param day  天数
	 * @param style  预转换的日期格式
	 * @return
	 * @CreateDate: 2018年12月2日 下午6:03:22
	 * @throws Exception 异常
	 */
	public static String getBeforeDay(String beforeDate, int day, String style) {
		if (ChkUtil.isEmpty(style)) {
			style = STYLE_1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		Date date = null;
		try {
			date = sdf.parse(beforeDate);// 初始日期
		} catch (Exception e) {

		}
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, -day);// 在日历的月份上减少n个月
		return sdf.format(now.getTime());
	}
	/**
	 * 获取当前时间年月日字符串
	 * @return
	 */
	public static String getyyyyMMdd() {
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_3);
		String str = sdf.format(new Date());
		return str;
	}
	
	/**
	 * 获取当前时间年月日字符串
	 * @return
	 */
	public static String gethhhhmmss() {
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_9);
		String str = sdf.format(new Date());
		return str;
	}
	
	/**
	 * 获取某个日期后面若干天
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateAfterDay(Date date, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, day);
		return now.getTime();
	}

	/**
	 * 拼接零分时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateOnZero(Date date) {
		return format(date) + " 00:00:00";
	}
	
	/**
	 * 获取传入时间的yyyy-MM-dd字符串
	 * @return
	 */
	public static String getDateStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_2);
		String str = sdf.format(date);
		return str;
	}
	
	public static String getpreDate(String str,int day) {
		SimpleDateFormat sdf = new SimpleDateFormat(STYLE_2);
		Date d;
		String nowTime = "";
		try {
			d = sdf.parse(str);
			Calendar no = Calendar.getInstance();
			no.setTime(d);
			no.set(Calendar.DATE, no.get(Calendar.DATE) - day);
			 nowTime = sdf.format(no.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nowTime;
	}
	public static void main(String[] args) {
	}
}
