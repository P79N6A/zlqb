package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 性别
 */
public enum Gender {
    Man("M","男"),
    Woman("F","女");

    private String type;
    private String description;



    Gender(String type, String description) {
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
