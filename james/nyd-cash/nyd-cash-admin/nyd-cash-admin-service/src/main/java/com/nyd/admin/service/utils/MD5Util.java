package com.nyd.admin.service.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Created by Dengw on 2018/1/8
 */
public class MD5Util {
    private static Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

    /**
     * 对字符串32位md5加密
     *
     * @param sourceStr
     * @return
     */
    public static String MD5(String sourceStr) throws Exception {
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
            LOGGER.error("MD5加密出现错误",e);
            throw new Exception("MD5加密出现错误");
        }
        return result;
    }
}
