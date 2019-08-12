package com.nyd.admin.model.enums;


import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CongYuxiang

 */
public enum FundSourceEnum {
    WSM("nyd1001","微神马"),
    KZJR("nyd1002","空中金融");



    private String code;
    private String name;

    FundSourceEnum(String code, String name){
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
        for (FundSourceEnum type : FundSourceEnum.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }
    public static FundSourceEnum toEnum(String code) {
        List<EnumModel> list = new ArrayList<>();
        for (FundSourceEnum type : FundSourceEnum.values()) {
           if(type.getCode().equals(code)){
               return type;
           }

        }
        return null;
    }
    public static String getFundSourceCode(String name) {
        List<EnumModel> list = new ArrayList<>();
        for (FundSourceEnum type : FundSourceEnum.values()) {
            if(type.getName().equals(name)){
                return type.getCode();
            }

        }
        return null;
    }

    public static Map<String,String> enumMap() {
        Map<String,String> map = new HashMap<>();

        for (FundSourceEnum type : FundSourceEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }

}
