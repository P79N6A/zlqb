package com.nyd.zeus.model;

/**
 * Cong Yuxiang
 * 2017/11/17
 **/
public enum PayModelEnum {
    WX("0","微信"),
    ZFB("1","支付宝"),
    YL("2","银联");

    private String code;
    private String info;

    PayModelEnum(String code, String info){
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }



    public String getInfo() {
        return info;
    }

}
