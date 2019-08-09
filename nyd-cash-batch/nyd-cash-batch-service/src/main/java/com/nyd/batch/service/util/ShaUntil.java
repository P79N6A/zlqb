package com.nyd.batch.service.util;

import com.nyd.batch.service.quartz.BillTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hwei on 2017/12/2.
 * sha 加密
 */
public class ShaUntil {
    private static Logger LOGGER = LoggerFactory.getLogger(ShaUntil.class);

    /**
     * sha512加密
     * @param str
     * @return
     */
    public static String SHA512(String str) {
        return  ShaUntil.SHA(str,"SHA-512");
    }


    public static String SHA(String str,String type) {
        String strResult = null;
        if (str!=null&&str.length()>0) {
            try {
                //创建加密对象，传入加密类型
                MessageDigest messageDigest = MessageDigest.getInstance(type);
                //传入要加密的字符串
                messageDigest.update(str.getBytes());
                //得到byte类型结果
                byte byteBuffer[] = messageDigest.digest();
                // 將 byte转换为 string
                StringBuffer strHexString = new StringBuffer();
                // 遍历 byte buffer
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回结果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("ShaUntil sha has except!",e);
            }
        }
        return strResult;
    }


}
