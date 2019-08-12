package com.nyd.order.model.enums;

public enum WithholdConfigCode {
    MIN_1("MIN_1","一分钟跑批编码"),
    MIN_10("MIN_10","十分钟跑批编码"),
    HOUR_1("HOUR_1","一小时跑批编码"),
    MIN_FAIL_1("MIN_FAIL_1","拒绝订单一分钟跑批编码"),
	MIN_FAIL_10("MIN_FAIL_10","拒绝订单十分钟跑批编码"),
	HOUR_FAIL_1("HOUR_FAIL_1","拒绝订单一小时跑批编码");

    private String code;

    private String msg;

    private WithholdConfigCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return this.code;
    }

    public Integer getCodeInt(){
        return Integer.valueOf(this.code);
    }

    public String getMsg(){
        return this.msg;
    }
}
