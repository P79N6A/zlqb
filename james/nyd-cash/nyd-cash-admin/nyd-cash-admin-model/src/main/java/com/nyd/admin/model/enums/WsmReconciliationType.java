package com.nyd.admin.model.enums;

import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/1
 **/
public enum WsmReconciliationType {
    ZCZD("0","正常账单"),
    TD("1","退单");


    private String code;
    private String name;

    WsmReconciliationType(String code, String name){
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
        for (WsmReconciliationType type : WsmReconciliationType.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }

}
