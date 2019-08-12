package com.nyd.admin.model.enums;


import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CongYuxiang

 */
public enum InUseEnum {
    QY("0","启用"),
    BQY("1","未启用");



    private String code;
    private String name;

    InUseEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static List<EnumModel> toMap() {
        List<EnumModel> list = new ArrayList<>();
        for (InUseEnum type : InUseEnum.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }
    public static Map<Integer,String> enumMap() {
        Map<Integer,String> map = new HashMap<>();

        for (InUseEnum type : InUseEnum.values()) {

            map.put(Integer.valueOf(type.getCode()),type.getName());

        }
        return map;
    }
   
}
