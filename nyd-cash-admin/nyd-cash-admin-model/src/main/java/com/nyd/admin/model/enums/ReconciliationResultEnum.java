package com.nyd.admin.model.enums;

import com.nyd.admin.model.EnumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/30
 **/
public enum ReconciliationResultEnum {
    ZC("0","正常"),
    WFY_DFMY("1","我方有对方没有"),
    WFMY_DFY("2","我方没有对方有"),
    SJBYZ("3","数据不一致");



    private String code;
    private String name;

    ReconciliationResultEnum(String code, String name){
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
        for (ReconciliationResultEnum type : ReconciliationResultEnum.values()) {
            EnumModel enumModel = new EnumModel();
            enumModel.setCode(type.getCode());
            enumModel.setName(type.getName());
            list.add(enumModel);

        }
        return list;
    }

    public static Map<String,String> enumMap() {
        Map<String,String> map = new HashMap<>();

        for (ReconciliationResultEnum type : ReconciliationResultEnum.values()) {

            map.put(type.getCode(),type.getName());

        }
        return map;
    }
}
