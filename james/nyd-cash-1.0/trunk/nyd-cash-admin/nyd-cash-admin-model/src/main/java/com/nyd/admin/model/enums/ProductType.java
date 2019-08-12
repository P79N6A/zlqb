package com.nyd.admin.model.enums;

import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dengw on 2017/12/15
 */
public enum ProductType {
    ONE("0","单期"),
    MANY("1","多期");

    private String code;
    private String name;

    ProductType(String code, String name){
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
        for (ProductType type : ProductType.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }
    public static Map<Integer,String> enumMap() {
        Map<Integer,String> map = new HashMap<>();

        for (ProductType type : ProductType.values()) {

            map.put(Integer.valueOf(type.getCode()),type.getName());

        }
        return map;
    }
}
