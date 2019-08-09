package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 与联系人关系
 */
public enum RelationShipEnum {
    Spouse("1","配偶"),
    Parent("2","父母"),
    BrothersAndSisters("3","兄弟姐妹"),
    Relative("4","亲戚"),
    Colleague("5","同事"),
    Friend("6","朋友"),
    Others("7","其他");

    private String type;
    private String description;



    RelationShipEnum(String type, String description) {
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
