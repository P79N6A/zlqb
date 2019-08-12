package com.creativearts.nyd.web;

/**
 * Cong Yuxiang
 * 2018/1/10
 **/
public class App {
    public static void main(String[] args) {
        String s = "101515135662580001-1515556437049-z";
        String[] ss = s.split("-");
        String type=null;
        if(ss.length==3){
            type = ss[2];
        }
        System.out.println(type);

    }
}
