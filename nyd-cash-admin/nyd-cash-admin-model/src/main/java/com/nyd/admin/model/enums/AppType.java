package com.nyd.admin.model.enums;

/**
 * Created by hwei on 2018/7/10.
 */
public enum AppType {
    /**
     * type 名不要起太长，后面需要用来组合生成协议编号，编号长度有限制小于32位
     */
   NYD("nyd",""),
    XQH("xqh","1"),
    XQB("xqb","2"),
    WWJ("wwj","3"),
    XQJ1("xqj1","4"),  //享钱借
    XQJ2("xqj2","5");  //享钱进

    private String type;
    private String description;

    AppType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
