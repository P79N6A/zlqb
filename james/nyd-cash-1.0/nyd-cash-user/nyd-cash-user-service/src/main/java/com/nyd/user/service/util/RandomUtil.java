package com.nyd.user.service.util;

import java.util.Random;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 16:39 2018/6/12
 */
public class RandomUtil {

    /**
     * 生成length长度的数字字母组合随机数
     * @param length
     * @return
     */
    public static String randomNumber(int length){
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();//随机用以下三个随机生成器
        Random randdata=new Random();
        int data=0;
        for(int i=0;i<length;i++)
        {
            int index=rand.nextInt(3);
            //目的是随机选择生成数字，大小写字母
            switch(index)
            {
                case 0:
                    data=randdata.nextInt(10);//仅仅会生成0~9
                    sb.append(data);
                    break;
                case 1:
                    data=randdata.nextInt(26)+65;//保证只会产生65~90之间的整数
                    sb.append((char)data);
                    break;
                case 2:
                    data=randdata.nextInt(26)+97;//保证只会产生97~122之间的整数
                    sb.append((char)data);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 生成引流随机密码 首位小写字母，后面随机数字
     * @return
     */
    public static String flowPasswd(int length){
        StringBuilder sb=new StringBuilder();
        Random randdata=new Random();
        String firstIndex= ""+ ((char)(randdata.nextInt(26)+97));
        System.out.println(firstIndex);
        int data=0;
        for(int i=1;i<length;i++) {
            data=randdata.nextInt(10);//仅仅会生成0~9
            sb.append(data);
        }
        String remainIndexs = sb.toString();
        System.out.println(remainIndexs);
        return firstIndex+remainIndexs;
    }

    /**
     * 获取指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandom(int length){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
    public static void main(String[] args) {
        System.out.println(getRandom(14));
    }
}
