package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @create 2018-01-16 16:12
 **/
public enum FundCodeEnum {
    kzjr("kzjr","空中金融");

    private String code;
    private String name;

    FundCodeEnum(String code, String name){
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

        for (FundCodeEnum type : FundCodeEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }
}
