package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 联系人类型
 */
public enum ContactType {
    Direct("Direct","直接联系人"),
    Major("Major","重要联系人");
    private String type;
    private String description;



    ContactType(String type, String description) {
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
