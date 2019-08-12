package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum InvitationCodeFlagEnum {
    zero(0,"初始化"),
    one(1,"未使用"),
    two(2,"已使用"),
    Three(3,"已过期");

    private Integer code;
    private String name;

    InvitationCodeFlagEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<Integer,String> enumMap() {
        Map<Integer,String> map = new HashMap<>();
        for (InvitationCodeFlagEnum type : InvitationCodeFlagEnum.values()) {
            map.put(type.getCode(),type.getName());
        }
        return map;
    }
}
