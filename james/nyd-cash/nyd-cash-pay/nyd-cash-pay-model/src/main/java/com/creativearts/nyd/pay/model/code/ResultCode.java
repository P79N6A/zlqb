package com.creativearts.nyd.pay.model.code;

/**
 * 返回的代码
 */
public enum ResultCode {

    PAY_SUCESS("9000","成功"),
    PAY_TYPE_FAIL("9001","类型转换失败");


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
