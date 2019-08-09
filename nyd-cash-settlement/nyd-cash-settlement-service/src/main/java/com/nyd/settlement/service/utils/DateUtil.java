package com.nyd.settlement.service.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dengw on 2018/1/18
 */
public class DateUtil {
    /**
     * 传入的日期 必须是 yyyy-MM-dd 否则失败
     * @param calcuDate
     * @param promiseDate
     * @return
     */
    public static int calcuteDateBetween(String calcuDate,String promiseDate){
        long calcuteTime = 0;
        try {
            calcuteTime = DateUtils.parseDate(calcuDate,"yyyy-MM-dd").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long promiseTime = 0;
        try {
            promiseTime = DateUtils.parseDate(promiseDate,"yyyy-MM-dd").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(calcuteTime<=promiseTime){
            return 0;
        }else {
            return (int)((calcuteTime-promiseTime)/(1000*3600*24));
        }
    }

    /**
     * 时间格式转换yyyy-mm-dd hh:ss:mm 转成yyyy-mm-dd
     */
    public static String transferFormat(String inTime) throws ParseException {
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
        Date tempDate =null;
        String outTime = null;
        tempDate = s1.parse(inTime);
        outTime = s2.format(s2.parse(s1.format(tempDate)));
        return outTime;
    }
}
