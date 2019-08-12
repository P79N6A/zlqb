package com.nyd.admin.service.excel;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public class XSSFDateUtil extends DateUtil {
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}
