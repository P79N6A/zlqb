package com.nyd.user.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Dengw on 2017/11/24
 */
@Component
public class Md5Util {
    @Autowired
    private UserProperties userProperties;

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new Exception("MD5加密出现错误");
        }
    }

    /**
     * md5两次加密
     *
     * @param str
     * @return
     */
    public String getSecondMD5(String str) throws Exception {
        str += userProperties.getMd5Key();
        String first = getMD5(str);
        first += userProperties.getMd5Key();
        String second = getMD5(first);
        return second;
    }
    
    /**
     * 获取md5 32位小写
     * @param str
     * @return
     */
    public static String getMD5To32LowCaseSign(String str){
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();
            int i;
            for(int offset=0; offset<b.length; offset++){
                i = b[offset];
                if(i<0){
                    i+=256;
                }
                if(i<16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return buf.toString();
    }
}
