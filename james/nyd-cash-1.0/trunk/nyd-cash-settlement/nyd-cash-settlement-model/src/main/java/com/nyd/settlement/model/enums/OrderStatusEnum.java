package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusEnum {

    SHZ(10,"审核中"),
    SHWZ(20,"审核完成"),
    DFK(30,"待放款"),
    FKSB(40,"放款失败"),
    YFK(50,"已放款");

    private Integer code;
    private String name;

    OrderStatusEnum(Integer code, String name){
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
        for (OrderStatusEnum type : OrderStatusEnum.values()) {
            map.put(type.getCode(),type.getName());
        }
        return map;
    }

}
