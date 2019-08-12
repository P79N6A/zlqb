package com.nyd.zeus.service.enums;

/**
 * Created by zhujx on 2017/11/21.
 */
public enum CommonEnum {

    SUCESS("R001","成功"),
    FAILURE("R002","失败"),
    REAL_TIME("1","实时"),
    DELAY_TIME("2","T+1"),
    REPAY_INITIATIVE("1","主动还款"),
    REPAY_PASSIVITY("1","代扣");

    CommonEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
