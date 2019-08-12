package com.nyd.user.model.enums;

import java.io.Serializable;

public enum UniteRegisterRespCode implements Serializable{
	
	REGISTER_NEW_SUCCESS(200,"新用户注册成功"),

    REGISTER_OLD_SUCCESS(1000,"商户老用户注册"),

    REGISTER_FAIL(1001,"注册失败"),

    SERVER_ERROR(1002,"服务内部错误"),

    SIGN_ERROR(1003,"验证签名错误");

    private int code;

    private String message;

    UniteRegisterRespCode(int code,String message){
        this.code = code;

        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
