package com.nyd.settlement.model.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @create 2018-01-16 16:12
 **/
public enum IsTestEnum {
    yes(0,"测试申请"),
    no(1,"正式申请");

    private Integer code;
    private String name;

    IsTestEnum(Integer code, String name){
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
        for (IsTestEnum type : IsTestEnum.values()) {
            map.put(type.getCode(),type.getName());
        }
        return map;
    }
}
