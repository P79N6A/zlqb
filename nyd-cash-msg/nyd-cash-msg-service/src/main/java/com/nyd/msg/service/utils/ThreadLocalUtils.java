package com.nyd.msg.service.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Yuxiang Cong
 **/
public class ThreadLocalUtils {
    public static final String BATCH = "batch";
    public static final String SINGLE = "single";
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();
    public static void setThreadLocalFlag(String flag) {
        TL.set(flag);
    }

    public static void removeThreadLocalFlag() {
        TL.remove();
    }

    public static String getFlag() {
        String flag = TL.get();
        if (StringUtils.isBlank(flag)) {
           return SINGLE;
        }
        return flag;
    }
}
