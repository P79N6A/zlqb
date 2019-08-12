package com.nyd.capital.service.jx.util;

import java.util.Random;

/**
 * 密码生成工具
 * @author liuqiu
 */
public class PasswordUtil {
    public static String getPassword(){
        Random random = new Random();
        String result="";
        for (int i=0;i<6;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }
}
