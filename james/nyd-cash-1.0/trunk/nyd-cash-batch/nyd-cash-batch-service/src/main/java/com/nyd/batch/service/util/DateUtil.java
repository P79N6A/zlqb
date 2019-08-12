package com.nyd.batch.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhujx on 2017/11/20.
 */
public class DateUtil {

    /**
     * 计算两个时间相差天数
     * @param beginDate
     * @param endDate
     * @return
     */
     public static int getDay(Date beginDate,Date endDate) {
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
