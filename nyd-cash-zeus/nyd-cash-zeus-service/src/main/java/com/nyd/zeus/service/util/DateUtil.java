package com.nyd.zeus.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/20.
 */
public class DateUtil {

	public final static String STYLE_1 = "yyyy-MM-dd HH:mm:ss";

	
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

    public static Date string2Date(String value,String format){
        if(value == null || "".equals(value)){
            return null;
        }

        SimpleDateFormat sdf = getFormat(format);
        Date date = null;

        try {
            date = sdf.parse(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static SimpleDateFormat getFormat(String format){
        if(format == null || "".equals(format)){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(format);
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

    /**
     * 传入日期加上指定天数
     * @param borrowTime
     * @return
     */
    public static Date addDayFromRemitTime(Date remittime,int borrowTime) {
        Date today = remittime;
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, borrowTime);// 今天+borrowTime天

        Date date = c.getTime();
        return date;
    }

    public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}
    /**
     * 获取某个日期为星期几
     * @param date
     * @return String "sunday"
     */
    public static String getDayWeekOfDate1(Date date) {
        String[] weekDays = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
}
