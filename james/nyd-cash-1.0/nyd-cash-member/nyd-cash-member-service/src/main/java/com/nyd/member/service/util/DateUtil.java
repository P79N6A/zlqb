package com.nyd.member.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dengw on 2017/11/20
 * 日期工具类
 */
public class DateUtil {
    private static String forMat = "yyyy-MM-dd";

    public static String dateToString(Date date){
        String str = null;
        SimpleDateFormat sdf = new SimpleDateFormat(forMat);
        if(date != null){
            str = sdf.format(date);
        }
        return str;
    }
}
