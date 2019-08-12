package com.nyd.settlement.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: CongYuxiang

 */
public enum DerateTypeEnum {
    BJM(0,"不减免"),
    LX(1,"利息"),
    FX(2,"罚息"),
    ZNJ(3,"滞纳金"),
    LXFX(4,"利息加罚息"),
    LXFXZNJ(5,"利息加罚息加滞纳金"),
    QT(6,"其他");

    private Integer code;
    private String name;

    DerateTypeEnum(Integer code, String name){
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

        for (DerateTypeEnum type : DerateTypeEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }

}
