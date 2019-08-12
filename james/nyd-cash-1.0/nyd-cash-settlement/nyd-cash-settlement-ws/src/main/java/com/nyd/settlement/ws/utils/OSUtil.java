package com.nyd.settlement.ws.utils;

public class OSUtil {
    private static final String os = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return os.indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        return os.indexOf("windows") >= 0;
    }
}
