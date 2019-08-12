package com.nyd.zeus.service.enums;

/**
 * Created by zhujx on 2017/11/20.
 */
public enum BillStatusEnum {

    REPAY_ING("B001","还款中"),
    REPAY_OVEDUE("B002","逾期中"),
    REPAY_SUCCESS("B003","已结清");

    BillStatusEnum(String code, String value) {
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
