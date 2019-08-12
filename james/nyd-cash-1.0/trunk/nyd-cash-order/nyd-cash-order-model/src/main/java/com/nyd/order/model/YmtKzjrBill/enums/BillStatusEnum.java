package com.nyd.order.model.YmtKzjrBill.enums;

import org.apache.commons.lang3.StringUtils;

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

    public static BillStatusEnum getByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        for (BillStatusEnum bs:BillStatusEnum.values()){
            if(bs.getCode().equals(code)){
                return bs;
            }
        }
        return null;
    }
}
