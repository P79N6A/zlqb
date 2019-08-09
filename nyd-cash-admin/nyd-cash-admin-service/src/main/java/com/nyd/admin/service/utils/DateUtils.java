package com.nyd.admin.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	protected final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	 
	public static final String DATE_SHORT_FORMAT = "yyyyMMdd";
	public static final String DATE_TIME_SHORT_FORMAT = "yyyyMMddHHmm";
	public static final String DATE_TIMESTAMP_SHORT_FORMAT = "yyyyMMddHHmmss";
	public static final String DATE_TIMESTAMP_LONG_FORMAT = "yyyyMMddHHmmssS";
	public static final String DATE_CH_FORMAT = "yyyy年MM月dd日";
	
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String TIME_SHORT_FORMAT = "HHmmss";

	public static final String DAYTIME_START = "00:00:00";
	public static final String DAYTIME_END = "23:59:59";
	
	private DateUtils() {
	}

	private static final String[] FORMATS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "HH:mm",
			"HH:mm:ss", "yyyy-MM", "yyyy-MM-dd HH:mm:ss.S","yyyy-MM-dd HH:mm:ss" ,"yyyyMMddHHmmss"};

	public static Date convert(String str) {
		if (str != null && str.length() > 0) {
			if (str.length() > 10 && str.charAt(10) == 'T') {
				str = str.replace('T', ' '); // 去掉json-lib加的T字母
			}
			for (String format : FORMATS) {
				if (str.length() == format.length()) {
					try {
						Date date = new SimpleDateFormat(format).parse(str);
						return date;
					} catch (ParseException e) {
						if (logger.isWarnEnabled()) {
							logger.warn(e.getMessage(),e);
						}
					}
				}
			}
		}
		return null;
	}

	public static Date convert(String str, String format) {
		if (!StringUtils.isEmpty(str)) {
			try {
				Date date = new SimpleDateFormat(format).parse(str);
				return date;
			} catch (ParseException e) {
				if (logger.isWarnEnabled()) {
					logger.warn(e.getMessage(),e);
				}
				// logger.warn(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 时间拼接 将日期和实现拼接 ymd 如2012-05-15 hm 如0812
	 * 
	 *  
	 * @date 2012-11-22 下午4:48:43
	 */
	public static Date concat(String ymd, String hm) {
		if (!StringUtils.isEmpty(ymd) && !StringUtils.isEmpty(hm)) {
			try {
				String dateString = ymd.concat(" ").concat(
						hm.substring(0, 2).concat(":").concat(hm.substring(2, 4)).concat(":00"));
				Date date = DateUtils.convert(dateString, DateUtils.DATE_TIME_FORMAT);
				return date;
			} catch (NullPointerException e) {
				if (logger.isWarnEnabled()) {
					logger.warn(e.getMessage(),e);
				}
			}
		}
		return null;
	}

	/**
	 * 根据传入的日期返回年月日的6位字符串，例：20101203
	 *  
	 * @date 2012-11-28 下午8:35:55
	 */
	public static String getDay(Date date) {
		return convert(date, DATE_SHORT_FORMAT);
	}

	/**
	 * 根据传入的日期返回中文年月日字符串，例：2010年12月03日
	 *  
	 * @date 2012-11-28 下午8:35:55
	 */
	public static String getChDate(Date date) {
		return convert(date, DATE_CH_FORMAT);
	}
	
        /**
         * 将传入的时间格式的字符串转成时间对象
         * 
         * 例：传入2012-12-03 23:21:24
 
         * @date 2012-11-29 上午11:29:31
         */
        public static Date strToDate(String dateStr) {
        	SimpleDateFormat formatDate = new SimpleDateFormat(DATE_TIME_FORMAT);
        	Date date=null;
        	try{
        	    date = formatDate.parse(dateStr);
        	}catch(Exception e){
        	    
        	}
        	return date;
        }
	
	public static String convert(Date date) {
		return convert(date, DATE_TIME_FORMAT);
	}

	public static String convert(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}

		if (null == dateFormat) {
			dateFormat = DATE_TIME_FORMAT;
		}

		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**
	 * 返回该天从00:00:00开始的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDatetime(Date date) {
		String thisdate = convert(date, DATE_FORMAT);
		return convert(thisdate + " " + DAYTIME_START);

	}

	/**
	 * 返回n天后从00:00:00开始的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDatetime(Date date, Integer diffDays) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		String thisdate = df.format(date.getTime() + 1000l * 24 * 60 * 60 * diffDays);
		return convert(thisdate + " " + DAYTIME_START);
	}

	/**
	 * 返回该天到23:59:59结束的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndDatetime(Date date) {
		String thisdate = convert(date, DATE_FORMAT);
		return convert(thisdate + " " + DAYTIME_END);

	}

	/**
	 * 返回n天到23:59:59结束的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndDatetime(Date date, Integer diffDays) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		String thisdate = df.format(date.getTime() + 1000l * 24 * 60 * 60 * diffDays);
		return convert(thisdate + " " + DAYTIME_END);

	}

	/**
	 * 返回该日期的最后一刻，精确到纳秒
	 * 
	 * @return
	 */
	public static Timestamp getLastEndDatetime(Date endTime) {
		Timestamp ts = new Timestamp(endTime.getTime());
		ts.setNanos(999999999);
		return ts;
	}

	/**
	 * 返回该日期加1秒
	 * 
	 * @return
	 */
	public static Timestamp getEndTimeAdd(Date endTime) {
		Timestamp ts = new Timestamp(endTime.getTime());
		Calendar c = Calendar.getInstance();
		c.setTime(ts);
		c.add(Calendar.MILLISECOND, 1000);
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis());
	}

	/**
	 * 相对当前日期，增加或减少天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDay(Date date, int day) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);

		return df.format(new Date(date.getTime() + 1000l * 24 * 60 * 60 * day));
	}

	/**
	 * 相对当前日期，增加或减少天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDayToDate(Date date, long day) {
		return new Date(date.getTime() + 1000 * 24 * 60 * 60 * day);
	}

	/**
	 * 返回两个时间的相差天数
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差天数
	 */
    @Deprecated
	public static Long getTimeDiff(String startTime, String endTime) {

		return getDayDiff(startTime,endTime);
	}

	/**
	 * 返回两个时间的相差天数
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差天数
	 */
    @Deprecated
	public static Long getTimeDiff(Date startTime, Date endTime) {
		return getDayDiff(startTime,endTime);
	}

    /**
     * 返回两个时间的相差天数
     *
     * @param startTime
     *            对比的开始时间
     * @param endTime
     *            对比的结束时间
     * @return 相差天数
     */
    public static Long getDayDiff(String startTime, String endTime) {
        Long days = null;
        Date startDate = null;
        Date endDate = null;
        try {
            if (startTime.length() == 10 && endTime.length() == 10) {
                startDate = new SimpleDateFormat(DATE_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_FORMAT).parse(endTime);
            } else {
                startDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(endTime);
            }

            days = getDayDiff(startDate, endDate);
        } catch (ParseException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage());
            }
            days = null;
        }
        return days;
    }

    /**
     * 返回两个时间的相差天数
     *
     * @param startTime
     *            对比的开始时间
     * @param endTime
     *            对比的结束时间
     * @return 相差天数
     */
    public static Long getDayDiff(Date startTime, Date endTime) {
        Long days = null;

        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        long l_s = c.getTimeInMillis();
        c.setTime(endTime);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        long l_e = c.getTimeInMillis();
        days = (l_e - l_s) / 86400000;
        return days;
    }

    /**
	 * 返回两个时间的相差分钟数
	 * 
	 * @param startTime
	 *            对比的开始时间
	 * @param endTime
	 *            对比的结束时间
	 * @return 相差分钟数
	 */
	public static Long getMinuteDiff(Date startTime, Date endTime) {
		Long minutes = null;

		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		long l_s = c.getTimeInMillis();
		c.setTime(endTime);
		long l_e = c.getTimeInMillis();
		minutes = (l_e - l_s) / (1000l * 60);
		return minutes;
	}

    /**
     * 返回两个时间的相差秒数
     * @param startTime
     *            对比的开始时间
     * @param endTime
     *            对比的结束时间
     * @return 相差秒数
     */
    public static Long getSecondDiff(Date startTime, Date endTime) {
        Long minutes = null;
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        long l_s = c.getTimeInMillis();
        c.setTime(endTime);
        long l_e = c.getTimeInMillis();
        minutes = (l_e - l_s) / 1000l ;
        return minutes;
    }
    /**
     * 返回两个时间的相差秒数
     * @param startTime
     *            对比的开始时间
     * @param endTime
     *            对比的结束时间
     * @return 相差秒数
     */
    public static Long getSecondDiff(String startTime, String endTime) {
        Long seconds = null;
        Date startDate = null;
        Date endDate = null;
        try {
            if (startTime.length() == 10 && endTime.length() == 10) {
                startDate = new SimpleDateFormat(DATE_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_FORMAT).parse(endTime);
            } else {
                startDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(startTime);
                endDate = new SimpleDateFormat(DATE_TIME_FORMAT).parse(endTime);
            }

            seconds = getSecondDiff(startDate, endDate);
        } catch (ParseException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(e.getMessage());
            }
            seconds = null;
        }
        return seconds;
    }

	public static String getPidFromDate(Date date) {
		if (date == null) {
			return "";
		}

		String m = convert(date, "yyyyMM");
		String d = convert(date, "dd");

		if (Integer.valueOf(d) <= 10) {
			d = "01";
		} else if (Integer.valueOf(d) <= 20) {
			d = "02";
		} else {
			d = "03";
		}

		return m.concat(d);
	}


	/**
	 * @title 判断是否为工作日
	 * @param dateStr
	 *            日期 2015-11-06
	 * @return 是工作日返回true，非工作日返回false
	 * @throws Exception
	 */
	public static boolean isWeekday(String dateStr) throws Exception {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setTime(sdf.parse(dateStr));
		if (calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SATURDAY
				&& calendar.get(GregorianCalendar.DAY_OF_WEEK) != GregorianCalendar.SUNDAY) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否一周的某天 如周三 WEDNESDAY = 4
	 *
	 * @param dateStr
	 * @param gregorianCalendarDay
	 * @return
	 * @throws Exception
	 */
	public static boolean isSomeDay(String dateStr, int gregorianCalendarDay) throws Exception {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setTime(sdf.parse(dateStr));
		if (calendar.get(GregorianCalendar.DAY_OF_WEEK) == gregorianCalendarDay) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否是日期，用于新、旧银联账单的判断
	 *
	 * @param datestr
	 * @return
	 */
	public static boolean isDateStr(String datestr) {
		if (StringUtils.isNotBlank(datestr)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			try {
				sf.parse(datestr);
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 *
	 * <p>
	 * 获取日期的月份数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 上午9:51:50
	 * @param dateStr
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static int getDateMonth(String dateStr) throws Exception {
		if (isDateStr(dateStr)) {// 是日期格式
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			calendar.setTime(sdf.parse(dateStr));
			return calendar.get(Calendar.MONTH) + 1;
		}
		return 0;
	}

	/**
	 *
	 * <p>
	 * 查询距今天数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:50:16
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int daysBetweenNow(String dateStr) throws ParseException {
		if (isDateStr(dateStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return daysBetween(sdf.parse(dateStr), new Date());
		}
		return -1;
	}

	public static int daysBetweenNowAll(String dateStr) throws ParseException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return daysBetween(sdf.parse(dateStr), new Date());
		} catch (Exception e) {
			System.out.println("exception :非法数据" + dateStr);
		}
		return -1;
	}

	/**
	 *
	 * <p>
	 * 查询距今天数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:50:16
	 * @param time
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int longStrDaysBetweenNow(String time) throws ParseException {
		if (StringUtils.isNotBlank(time)) {
			long timeLong = 0;
			try {
				timeLong = Long.valueOf(time);
			} catch (Exception e) {
				System.out.println("exception :非法数据" + time);
			}
			if (timeLong > 0) {
				long temp = timeLong * 1000;
				Date date = new Date(temp);
				return daysBetween(date, new Date());
			}

		}
		return -1;
	}

	/**
	 *
	 * <p>
	 * 计算两个日期之间相差的天数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:45:54
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 *
	 * <p>
	 * 两个日期间隔天数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:41:35
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		if (isDateStr(smdate) && isDateStr(bdate)) {// 是日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		}
		return -1;
	}

	/**
	 * 判断字符串是否是日期，用于新、旧银联账单的判断
	 * @param datestr
	 * @param sf
	 * @return
	 */
	public static boolean isDateStr(String datestr, SimpleDateFormat sf) {
		if (StringUtils.isNotBlank(datestr)) {
			try {
				sf.parse(datestr);
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 *
	 * <p>
	 * 两个日期间隔年数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:41:35
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int yearsBetween(String smdate, String bdate, SimpleDateFormat sdf) throws ParseException {
		try {
			if (isDateStr(smdate, sdf) && isDateStr(bdate, sdf)) {// 是日期格式
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(sdf.parse(smdate));
				long time1 = cal1.get(Calendar.YEAR);
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(sdf.parse(bdate));
				long time2 = cal2.get(Calendar.YEAR);
				long between_years = time2 - time1;
				return Integer.parseInt(String.valueOf(between_years));
			}
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 *
	 * <p>
	 * 格式化日期
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午6:32:16
	 * @param dateStr
	 * @return
	 * @see
	 */
	public static Date parseDate(String dateStr) {
		Date date = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = (Date) df.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 *
	 * <p>
	 * 格式化日期
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午6:32:16
	 * @param dateStr
	 * @return
	 * @see
	 */
	public static Date parseDate(String dateStr, DateFormat df) {
		Date date = null;
		try {
			date = (Date) df.parse(dateStr);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 *
	 * <p>
	 * 日期格式转化
	 * </p>
	 *
	 * @author zhujunjie 2017年5月8日 下午3:47:26
	 * @date 2017年5月8日 下午3:47:26
	 * @param dataString
	 * @param df
	 * @return
	 * @see
	 */
	public static Date parseDateAnyFormat(String dataString, String df) {
		dataString = StringUtils.trim(dataString);
		if(StringUtils.equalsIgnoreCase(dataString, "NULL")) {
			return null;
		}
		Date date = null;
		try {
			if (StringUtils.isNotBlank(df)) {
				date = org.apache.commons.lang3.time.DateUtils.parseDate(dataString, df);
			} else {
				date = org.apache.commons.lang3.time.DateUtils.parseDate(dataString, FORMATS);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return date;
	}

	/**
	 *
	 * <p>
	 * Date转化为str
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午6:32:16
	 * @param date
	 * @return
	 * @see
	 */
	public static String parseDateToStr(Date date, DateFormat df) {
		String dateStr = null;
		if (df == null) {
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		try {
			dateStr = df.format(date);
		} catch (Exception e) {
		}
		return dateStr;
	}

	/**
	 * 获取当前日期days之前的日期
	 *
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static String getDaysBeforeDate(String curDate, int days) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(curDate));
		} catch (ParseException e) {
			c.setTime(new Date());
		}
		c.add(Calendar.DATE, -days);
		Date monday = c.getTime();
		String preDay = sdf.format(monday);
		return preDay;
	}

	/**
	 *
	 * <p>
	 * 计算两个日期之间相差的小时数,忽略分钟数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:45:54
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int hoursBetween(long time1, long time2) throws ParseException {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			long temp1 = time1 * 1000;
			Date date1 = new Date(temp1);
			long temp2 = time2 * 1000;
			Date date2 = new Date(temp2);
			date1 = sdf.parse(sdf.format(date1));
			date2 = sdf.parse(sdf.format(date2));

			time1 = date1.getTime() / 1000;
			time2 = date2.getTime() / 1000;
			long between_hours = (time2 - time1) / (3600);
			return Integer.parseInt(String.valueOf(between_hours));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 *
	 * <p>
	 * 计算两个日期之间相差的小时数,忽略分钟数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:45:54
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int daysBetween(long time1, long time2) throws ParseException {
		try {
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long temp1 = time1 * 1000;
			Date date1 = new Date(temp1);
			long temp2 = time2 * 1000;
			Date date2 = new Date(temp2);
			date1 = sdf.parse(sdf.format(date1));
			date2 = sdf.parse(sdf.format(date2));

			time1 = date1.getTime() / 1000;
			time2 = date2.getTime() / 1000;
			long between_days = (time2 - time1) / (3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;
	}

	/**
	 *
	 * <p>
	 * 计算两个日期之间相差的秒数
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2015年12月11日 下午2:45:54
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 * @see
	 */
	public static int secondsBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String convert(Long unixTimeStamp, String dateFormat) {
		if (unixTimeStamp == null) {
			return null;
		}

		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = "yyyyMMddHHmmss";
		}
		long temp = unixTimeStamp * 1000;
		Date date = new Date(temp);
		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**
	 *
	 * <p>
	 * 字符串转化为long
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2016年11月15日 下午4:22:03
	 * @param str
	 * @return
	 * @see
	 */
	public static long strToLong(String str) {
		Date time = parseDate(str);
		if (time != null) {
			long result = time.getTime() / 1000;
			if (result > 0) {
				return result;
			}
		}
		return 0;
	}

	/**
	 *
	 * <p>
	 * long转化为字符串
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2016年11月15日 下午4:22:03
	 * @param time
	 * @return
	 * @see
	 */
	public static String longToStr(Long time) {
		if (time != null && time > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long temp = time * 1000;
			Date date = new Date(temp);
			String strTime = parseDateToStr(date, sdf);
			if (StringUtils.isNotBlank(strTime)) {
				return strTime;
			}
		}
		return null;
	}

	/**
	 * 获取当前日期days之前的日期
	 *
	 * @param days
	 * @return
	 * @throws ParseException
	 */
	public static String getCuDaysBeforeDate(Date curDate, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(curDate);
		c.add(Calendar.DATE, -days);
		Date monday = c.getTime();
		String preDay = sdf.format(monday);
		return preDay;
	}

	/**
	 *
	 * <p>
	 * 获取第几周
	 * </p>
	 *
	 * @author zhujunjie
	 * @date 2016年12月3日 上午9:37:01
	 * @return
	 * @see
	 */
	public static int getWeekCnt(Date data1, Date data2) {
		int n = 0;
		if (data1 != null && data2 != null) {
			try {
				int days = daysBetween(data1, data2);
				if (days > 0) {
					n = days % 7 == 0 ? days / 7 : days / 7 + 1;
				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return n;
	}

	public static void main(String[] args) throws ParseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date c = parseDateAnyFormat("2016/11/15 16:00:00", null);
//		System.out.println(sdf.format(c));
//		Date b = parseDateAnyFormat("2016-11-15 17:00", null);
//		System.out.println(sdf.format(b));
//		Date date=1613013911
//		List<String> list=new LinkedList();
//		list.add(null);
//		list.add("ss");
//		List<String> list1=list.stream().filter(num-> StringUtils.isNotBlank(num)).map(o->o.toString()).collect(Collectors.toList());
//		System.out.println(list1.size());




	}


}