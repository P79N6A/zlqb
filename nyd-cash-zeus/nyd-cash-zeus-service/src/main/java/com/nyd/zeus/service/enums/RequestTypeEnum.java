package com.nyd.zeus.service.enums;

/**
 * Created by zhujx on 2017/11/20.
 */
public enum RequestTypeEnum {

	ACTIVE_REPAYMENT("1","主动还款"),
	BATCH_REPAYTASK("2","跑批还款"),
	BATCH_OVERDUETASK("3","逾期"),
	MANAGE_REPAYMENT("4","代扣"),
	FLAT_ACCOUNT("5","平账");

    RequestTypeEnum(String code, String value) {
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
