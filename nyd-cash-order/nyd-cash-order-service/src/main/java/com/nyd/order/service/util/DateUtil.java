package com.nyd.order.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Dengw on 2017/11/20
 * 日期工具类
 */
public class DateUtil {
    private static String forMatHms = "yyyy-MM-dd HH:mm:ss";
    private static String forMat = "yyyy-MM-dd";
    private static String forHms = "HH:mm:ss";
    private static String forMatymdhms = "yyyyMMddHHmmss";
    
    public static String dateToString(Date date){
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat(forMat);
        if(date != null){
            str = sdf.format(date);
        }
        return str;
    }

    public static String dateToStringHms(Date date){
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat(forMatHms);
        if(date != null){
            str = sdf.format(date);
        }
        return str;
    }
    public static String dateToHms(Date date){
    	String str = null;
    	SimpleDateFormat sdf = new SimpleDateFormat(forHms);
    	if(date != null){
    		str = sdf.format(date);
    	}
    	return str;
    }

    public static String dateToYmdhms(Date date){
    	String str = null;
    	SimpleDateFormat sdf = new SimpleDateFormat(forMatymdhms);
    	if(date != null){
    		str = sdf.format(date);
    	}
    	return str;
    }
    
    public static int getDayDiffUp(Date start, Date end) {
        if(start != null && end != null){
            double millsOfDay = 1000 * 60 * 60 * 24;
            double days = ((double)(end.getTime() - start.getTime()) / millsOfDay);
            return (int) Math.ceil(days);
        }
        return 0;
    }

    public static int getDayDiffDown(Date start, Date end) {
        if(start != null && end != null){
            long millsOfDay = 1000 * 60 * 60 * 24;
            long days = ((end.getTime() - start.getTime()) / millsOfDay);
            return (int) days;
        }
        return 0;
    }
    /**
     * 计算两个时间相差天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDay(Date beginDate,Date endDate) {
        long day = 0;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            beginDate = df.parse(df.format(beginDate));
            endDate = df.parse(df.format(endDate));
            day = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int)day;
    }
    
    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     * 
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static boolean isEffectiveDate(String now, String start, String end) {
    	String format = "HH:mm:ss";
        Date nowTime = null;
        Date startTime = null;
        Date endTime = null;
		try {
			nowTime = new SimpleDateFormat(format).parse(now);
			startTime = new SimpleDateFormat(format).parse(start);
			endTime = new SimpleDateFormat(format).parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar endC = Calendar.getInstance();
        endC.setTime(endTime);

        if (date.after(begin) && date.before(endC)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
	 * 
	 *  功能说明：根据当前日期计算出后面几个月后的日期或者 几天后的日期  panye  2015-5-13  @param nowTime 当时日期
	 * type{2 代表月 其它代表 日} style 指定格式化日期样式 默认 yyyy-MM-dd HH:mm:ss  @return   
	 *  @throws  ParseException 最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getLastTime(String nowTime, int type, int increment, String style) throws ParseException {
		if (style == null || "".equals(style)) {
			style = "yyyy-MM-dd HH:mm:ss";
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
	 *  功能说明：获得当前时间  panye  2014-11-29  @param style 时间类型 如果style 则默认返回yyyy-MM-dd
	 * HH:mm:ss  @return String 时间字符串     @throws  最后修改时间： 修改人：panye 修改内容： 修改注意点：
	 */
	public static String getCurrentTime(String style) {
		if (StringUtils.isEmpty(style)) {
			style = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(style);
		String str = sdf.format(new Date());
		return str;
	}
	
	/**
	 * 
	 *  功能说明：格式化日期  panye  2014-11-29  @param  times 预格式化的日期字符串 style 格式化后的样式 默认是
	 * yyyy-MM-dd HH:mm:ss  @return Date     @throws  ParseException 最后修改时间：
	 * 修改人：panye 修改内容： 修改注意点：
	 */
	public static Date formatDate(String times, String style) throws ParseException {
		if (style == null || "".equals(style)) {
			style = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(style);
		return sdf.parse(times);
	}
	
	  public static String format(Date date, String pattern) {
			if (date != null) {
				SimpleDateFormat df = new SimpleDateFormat(pattern);
				return df.format(date);
			}
			return null;
		}
	  
	  /**
	     * 当前日期加上指定天数
	     * @param borrowTime
	     * @return
	     */
	    public static Date addDay(int borrowTime) {
	        Date today = new Date();
	        Calendar c = Calendar.getInstance();
	        c.setTime(today);
	        c.add(Calendar.DAY_OF_MONTH, borrowTime);// 今天+borrowTime天

	        Date date = c.getTime();
	        return date;
	    }
}
