package com.tasfe.zh.base.model.commonEnum;

/**
 * Created by hwei on 2017/11/4.
 * 用户账户类型
 */
public enum UserAccountType {
    Mobile(10,"手机号");
    private int type;
    private String description;

    UserAccountType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType(){
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }
}
