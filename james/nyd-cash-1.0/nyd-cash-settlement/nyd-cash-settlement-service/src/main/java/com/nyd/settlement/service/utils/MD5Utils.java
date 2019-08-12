package com.nyd.settlement.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Cong Yuxiang
 * 2018/2/1
 **/
public class MD5Utils {

    static Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("MD5加密出现错误",e);
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
