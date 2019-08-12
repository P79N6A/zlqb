package com.tasfe.zh.base.model.commonEnum;


/**
 * Created by hwei on 2017/11/4.
 * 婚姻状态
 */
public enum MaritalStatus {
    Single("1","未婚"),
    Married("2","已婚"),
    Divorce("3","离异"),
    Widowed("4","丧偶");

    private String type;
    private String description;



    MaritalStatus(String type, String description) {
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
