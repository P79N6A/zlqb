package com.nyd.user.service.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2018/2/8
 **/
public class DateUtil {
    private static String forMat = "yyyy-MM-dd";

    public static String calYears(Date endDate,Date startDate){
        try {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(endDate);
            c2.setTime(startDate);
            if (c1.getTimeInMillis() < c2.getTimeInMillis()) return "一年以下";
            int year1 = c1.get(Calendar.YEAR);
            int year2 = c2.get(Calendar.YEAR);
            int month1 = c1.get(Calendar.MONTH);
            int month2 = c2.get(Calendar.MONTH);
            int day1 = c1.get(Calendar.DAY_OF_MONTH);
            int day2 = c2.get(Calendar.DAY_OF_MONTH);
            int yearInterval = year1 - year2;
            if (month1 < month2 || month1 == month2 && day1 < day2) yearInterval--;
            int monthInterval = (month1 + 12) - month2;
            monthInterval %= 12;
            int months = yearInterval * 12 + monthInterval;
            if(months<12){
                return "一年以下";
            }else if(months>=12 && months<36){
                return "一年至三年";
            }else if(months>=36 && months<60){
                return "三年至五年";
            }else {
                return "五年以上";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "一年以下";
        }
    }

    public static String dateToString(Date date){
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat(forMat);
        if(date != null){
            str = sdf.format(date);
        }
        return str;
    }

    /**
     * 计算两个时间相差天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDays(Date beginDate,Date endDate) {
        if(beginDate == null || endDate == null){
            return 0;
        }
        long day = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            beginDate = df.parse(df.format(beginDate));
            endDate = df.parse(df.format(endDate));
            day = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (int)day;
    }

}
