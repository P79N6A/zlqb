package com.nyd.application.service.commonEnum;

/**
 * Created by Dengw on 2017/11/23
 */
public enum MongoCollection {
    ADDRESSBOOK("addressBook","通讯录"),
    CALLINFO("callInfo","通话记录"),
    SMSINFO("smsInfo","短信"),
    DEVICEINFO("deviceInfo","设备信息"),
    BURIEDINFO("buriedInfo","埋点信息"),
    APPINFO("appInfo","app安装情况");

    private String code;

    private String msg;

    private MongoCollection(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
