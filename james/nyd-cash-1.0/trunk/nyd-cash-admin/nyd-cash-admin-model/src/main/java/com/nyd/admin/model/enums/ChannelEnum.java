package com.nyd.admin.model.enums;

import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peng
 * @create 2017-12-19 14:36
 **/
public enum ChannelEnum {
    KS("kuaishou","快手"),
    QLTG("qltg","魔汇"),
    NYD("nyd","侬要贷");



    private String code;
    private String name;

    ChannelEnum(String code, String name){
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
        for (ChannelEnum type : ChannelEnum.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }
    public static ChannelEnum toEnum(String code) {
        List<EnumModel> list = new ArrayList<>();
        for (ChannelEnum type : ChannelEnum.values()) {
            if(type.getCode().equals(code)){
                return type;
            }

        }
        return null;
    }
    public static String getFundSourceCode(String name) {
        List<EnumModel> list = new ArrayList<>();
        for (ChannelEnum type : ChannelEnum.values()) {
            if(type.getName().equals(name)){
                return type.getCode();
            }

        }
        return null;
    }

    public static Map<String,String> enumMap() {
        Map<String,String> map = new HashMap<>();

        for (ChannelEnum type : ChannelEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }

}
