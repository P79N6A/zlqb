package com.tasfe.zh.base.model.commonEnum;


/**
 * Created by hwei on 2017/11/4.
 * 月收入
 */
public enum IncomeEnum {
    one("1","2000以下"),
    two("2","2001-3000元"),
    three("3","3001-5000元"),
    four("4","5001-12000元"),
    five("5","15001-18000元"),
    six("6","18000以上元");

    private String type;
    private String description;



    IncomeEnum(String type, String description) {
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
