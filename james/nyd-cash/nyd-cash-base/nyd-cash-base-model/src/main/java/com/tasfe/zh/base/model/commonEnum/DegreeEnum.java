package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 学历
 */
public enum DegreeEnum {
    Doctor("10","博士"),
    Master("20","硕士"),
    Undergraduate("30","本科"),
    BigCollege("40","大专"),
    MiddleCollege("50","中专"),
    TechnicalSchool("60","技校"),
    HighSchool("70","高中"),
    MiddleSchool("80","初中"),
    PrimarySchool("90","小学"),
    Illiteracy("100","其他");
    private String type;
    private String description;



    DegreeEnum(String type, String description) {
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
