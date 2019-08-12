package com.nyd.capital.service.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/8
 **/
public class QuartzUtils {
    public static String getCronTime(Date time){
        if(time==null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH * * ?");


           String formatTimeStr = sdf.format(time);

        return formatTimeStr;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        System.out.println(getCronTime(sdf.parse("09:53:32")));
    }
}
