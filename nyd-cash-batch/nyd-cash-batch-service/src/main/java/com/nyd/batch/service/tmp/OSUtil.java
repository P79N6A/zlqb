package com.nyd.batch.service.tmp;

/**
 * 操作系统类型判断工具类
 * Cong Yuxiang
 * 2017/11/21
 */
public class OSUtil {

    private static final String os = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return os.indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        return os.indexOf("windows") >= 0;
    }

}
