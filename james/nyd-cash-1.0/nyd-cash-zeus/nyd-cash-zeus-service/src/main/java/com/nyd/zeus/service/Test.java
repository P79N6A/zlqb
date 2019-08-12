package com.nyd.zeus.service;

import com.nyd.zeus.service.excel.XSSFDateUtil;

import java.text.SimpleDateFormat;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public class Test {
    public static void main(String[] args){
//        XSSFDateUtil.isCellDateFormatted()
            //  如果是date类型则 ，获取该cell的date值
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(XSSFDateUtil.getJavaDate(43020));
        System.out.println(s);
    }
}
