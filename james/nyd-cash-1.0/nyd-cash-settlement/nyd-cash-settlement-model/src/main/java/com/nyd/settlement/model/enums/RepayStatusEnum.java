package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @create 2018-01-19 19:36
 **/
public enum RepayStatusEnum {
    zero("0","成功"),
    one("1","失败");

    private String code;
    private String name;

    RepayStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<String,String> enumMap() {
        Map<String,String> map = new HashMap<>();
        for (RepayStatusEnum type : RepayStatusEnum.values()) {
            map.put(type.getCode(),type.getName());
        }
        return map;
    }
}
