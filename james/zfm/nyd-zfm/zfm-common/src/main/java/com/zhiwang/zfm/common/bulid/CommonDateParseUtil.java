package com.zhiwang.zfm.common.bulid;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author chenqingming
 *@描述 时间格式化工具类
 */
public class CommonDateParseUtil {
	public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY = "yyyy";
	public static final String MM = "MM";
	public static final String DD = "dd";

	/**
	 * 
	 * @param date
	 * @param formatStr
	 * @return 
	 * @描述  格式化日期对象
	 */
	public static Date date2date(Date date, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String str = sdf.format(date);
		try {
			date = sdf.parse(str);
		} catch (Exception e) {
			return null;
		}
		return date;
	}

	/**
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 * @描述 时间对象转换成字符串
	 */
	public static String date2string(Date date, String formatStr) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		strDate = sdf.format(date);
		return strDate;
	}

	/**
	 * 
	 * @param timestamp
	 * @param formatStr
	 * @return
	 * @描述 sql时间对象转换成字符串
	 */
	public static String timestamp2string(Timestamp timestamp, String formatStr) {
		String strDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		strDate = sdf.format(timestamp);
		return strDate;
	}

	/**
	 * 
	 * @param dateString
	 * @param formatStr
	 * @return
	 * @描述 字符串转换成时间对象
	 */
	public static Date string2date(String dateString, String formatStr) {
		Date formateDate = null;
		DateFormat format = new SimpleDateFormat(formatStr);
		try {
			formateDate = format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return formateDate;
	}

	/**
	 * 
	 * @param date
	 * @return
	 * @描述 Date类型转换为Timestamp类型
	 */
	public static Timestamp date2timestamp(Date date) {
		if (date == null)
			return null;
		return new Timestamp(date.getTime());
	}

	/**
	 * 
	 * @return
	 * @描述  获得当前年份
	 */
	public static String getNowYear() {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY);
		return sdf.format(new Date());
	}

	/**
	 * 
	 * @return
	 * @描述  获得当前月份
	 */
	public static String getNowMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat(MM);
		return sdf.format(new Date());
	}
	
	/**
	 * 
	 * @return
	 * @描述 获得当前日期中的日
	 */
	public static String getNowDay(){
		SimpleDateFormat sdf = new SimpleDateFormat(DD);
		return sdf.format(new Date());
	}

	/**
	 * 
	 * @param time
	 * @return
	 * @描述 指定时间距离当前时间的中文信息
	 */
	public static String getLnow(long time) {
		Calendar cal = Calendar.getInstance();
		long timel = cal.getTimeInMillis() - time;
		if (timel / 1000 < 60) {
			return "1分钟以内";
		} else if (timel / 1000 / 60 < 60) {
			return timel / 1000 / 60 + "分钟前";
		} else if (timel / 1000 / 60 / 60 < 24) {
			return timel / 1000 / 60 / 60 + "小时前";
		} else {
			return timel / 1000 / 60 / 60 / 24 + "天前";
		}
	}
	
	public static String getshow(String starttime) throws ParseException{//发布时间+3天
		Date fabudate=(new SimpleDateFormat(CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS)).parse(starttime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(fabudate);
		cal.add(Calendar.DATE, 3);
		String d = (new SimpleDateFormat(CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS)).format(cal.getTime());//发布时间
		
		Date currentdate = new Date();//当前时间
		 DateFormat df = new SimpleDateFormat(CommonDateParseUtil.YYYY_MM_DD_HH_MM_SS);
		 Date dt1 = df.parse(d);
         if(currentdate.getTime() >dt1.getTime() ){
        	 return "0";//不显示
		}else{
			return "1";//显示
		}
	}
}

