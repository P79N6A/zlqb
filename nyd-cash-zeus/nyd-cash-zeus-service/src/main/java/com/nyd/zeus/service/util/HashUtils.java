package com.nyd.zeus.service.util;

/**
 * Cong Yuxiang
 * 2017/11/24
 **/
public class HashUtils {
    public static int hash(String key){
        int arraySize = 11113;          //数组大小一般取质数
        int hashCode = 0;
        for(int i=0;i<key.length();i++){        //从字符串的左边开始计算
            int letterValue = key.charAt(i) - 96;//将获取到的字符串转换成数字，比如a的码值是97，则97-96=1 就代表a的值，同理b=2；
            hashCode = ((hashCode << 5) + letterValue) % arraySize;//防止编码溢出，对每步结果都进行取模运算
        }
        return Math.abs(hashCode);
    }
}
