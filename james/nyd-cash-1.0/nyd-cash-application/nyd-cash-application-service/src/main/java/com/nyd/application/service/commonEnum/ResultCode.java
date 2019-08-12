package com.nyd.application.service.commonEnum;

/**
 * 返回码
 */
public enum ResultCode {

    SUCESS("0","成功"),
    REQUEST_PRARM_EXCEPTION("400","请求参数异常"),
    REQUEST_EXCEPTION("500","第三方数据源请求异常"),
    RESPONSE_EXCEPTION("600","第三方数据源响应异常");


    private String code;

    private String message;

    private ResultCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode(){
        return this.code;
    }
    public Integer getCodeInt(){
        return Integer.valueOf(this.code);
    }

    public String getMessage(){
        return this.message;
    }
}
