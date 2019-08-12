package com.nyd.capital.model.enums;


/**
 * @author liuqiu
 */

public enum PocketBankEnum {
    ICBC(1, "中国工商银行"),
    ABC(2, "中国农业银行"),
    CEB(3, "中国光大银行"),
    CPG(4, "中国邮政储蓄银行"),
    CPG1(4, "中国邮政银行"),
    CIB(5, "兴业银行"),
    SDB(6, "深圳发展银行"),
    CCB(7, "中国建设银行"), 
    BOC(9, "中国银行"),
    SPDB(10, "浦发银行"),
    SZD(11, "平安银行"),
    HXB(12, "华夏银行"),
    CITIC(13, "中信银行"),
    COMM(14, "交通银行"),
    CMSB(15, "民生银行"),
    CGB(16, "广发银行"),
    BCCB(17, "北京银行"),
    BOS(18, "上海银行");


    private int code;
    private String name;

    PocketBankEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static PocketBankEnum toEnum(int code) {
        for (PocketBankEnum type : PocketBankEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }

        }
        return null;
    }

}
