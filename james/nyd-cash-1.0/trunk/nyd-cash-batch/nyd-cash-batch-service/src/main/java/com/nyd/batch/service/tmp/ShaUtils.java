package com.nyd.batch.service.tmp;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Cong Yuxiang
 * 2017/12/11
 **/
public class ShaUtils {
    public static String getSha(String str){
        if(str==null){
            return null;
        }
        return DigestUtils.sha1Hex(str.getBytes());
    }
}
