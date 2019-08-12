package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 用户状态
 */
public enum UserStatus {
    Normal("10","正常"),
    Member("20","会员"),
    Frozen("30","冻结");

    private String type;
    private String description;



    UserStatus(String type, String description) {
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
