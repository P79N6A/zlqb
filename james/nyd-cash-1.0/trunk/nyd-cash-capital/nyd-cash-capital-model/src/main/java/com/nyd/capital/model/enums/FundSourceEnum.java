package com.nyd.capital.model.enums;



/**
 * @author: CongYuxiang

 */
public enum FundSourceEnum {
    WSM("nyd1001","微神马"),
    WT("wt","稳通"),
    KZJR("kzjr","空中金融"),
    QCGZ("qcgz","七彩格子"),
    DLD("dld","多来点"),
    KDLC("kdlc","口袋理财"),
    JX("jx","即信");


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


    public static FundSourceEnum toEnum(String code) {
        for (FundSourceEnum type : FundSourceEnum.values()) {
           if(type.getCode().equals(code)){
               return type;
           }

        }
        return null;
    }

}
