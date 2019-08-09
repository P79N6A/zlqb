package com.nyd.settlement.model.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Peng
 * @create 2018-01-16 16:12
 **/
public enum ProductTypeEnum {
    single(0,"单期"),
    many(1,"多期");

    private Integer code;
    private String name;

    ProductTypeEnum(Integer code, String name){
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

        for (ProductTypeEnum type : ProductTypeEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }
}
