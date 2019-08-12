package com.nyd.msg.service;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Yuxiang Cong
 **/
public class TestApp {
    public static void main(String[] args) throws Exception{
//        List<String> mobiles = new ArrayList<>();
//        mobiles.add("15618624753");
//        mobiles.add("1234567890");
//        StringBuilder sb = new StringBuilder();
//        for(String mobile:mobiles){
//            sb.append(mobile).append(";");
//
//        }
//        System.out.println(sb.substring(0,sb.length()-1).toString());
        BufferedReader br = new BufferedReader(new FileReader("E:/tmp/yinhang.txt"));
        String s;
        while ((s = br.readLine())!=null){
            String[] ss = s.split("=>");
            System.out.println("map.put(\""+ss[1].trim()+"\",\""+ss[0].trim()+"\");");
        }
    }
}
