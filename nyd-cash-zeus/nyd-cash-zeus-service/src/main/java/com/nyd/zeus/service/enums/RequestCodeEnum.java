package com.nyd.zeus.service.enums;

/**
 * Created by zhujx on 2017/11/20.
 */
public enum RequestCodeEnum {

	REQUEST_SUCCESS("1","成功"),
	REQUEST_FAIL("2","失败"),
	REQUEST_ING("3","处理中"),
	REDUCTION("4","减免");
	

    RequestCodeEnum(String code, String value) {
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
