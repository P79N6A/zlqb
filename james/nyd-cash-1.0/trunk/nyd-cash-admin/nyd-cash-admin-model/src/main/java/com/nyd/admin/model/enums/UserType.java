package com.nyd.admin.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwei on 2018/01/08
 */
public enum UserType {
    INSIDE(1,"内部"),
    OUTSIDE(2,"外部");

    private Integer code;
    private String name;

    UserType(Integer code, String name){
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

        for (UserType type : UserType.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }
}
