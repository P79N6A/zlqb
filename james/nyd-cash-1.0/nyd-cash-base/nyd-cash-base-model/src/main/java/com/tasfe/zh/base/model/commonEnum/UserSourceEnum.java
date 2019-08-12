package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 用户来源
 */
public enum UserSourceEnum {
    NYD("NYD","app注册");

    private String type;
    private String description;



    UserSourceEnum(String type, String description) {
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
