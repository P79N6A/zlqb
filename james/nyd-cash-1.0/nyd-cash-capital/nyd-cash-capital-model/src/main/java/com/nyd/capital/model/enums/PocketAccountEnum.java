package com.nyd.capital.model.enums;


/**
 * @author liuqiu
 */

public enum PocketAccountEnum {
    Not_Open_Account("0", "未开户"),
    Open_Account("1", "已开户"),
    Have_Accepted("2", "已接受"),
    Not_Accepted("3", "未接受"),
    Withdrawal_Ing("4", "发起提现"),
    Have_Withdrawal("5", "已提现"),
    Have_Finish("6", "已完成"),
    Open_Account_Fail("7", "开户失败"),
    Open_Account_Ing("8", "开户中");


    private String code;
    private String name;

    PocketAccountEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static PocketAccountEnum toEnum(String code) {
        for (PocketAccountEnum type : PocketAccountEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }

        }
        return null;
    }

}
