package com.nyd.user.model.enums;

public enum RegisterGeiNiHuaEnums {
	NEW_SUCCESS("200","新用户"),
	SUCCESS_CODE("200","新用户注册成功"),
	OLD_SUCCESS("1000","本渠道老用户"),
	REG_FAIL("1001","注册失败"),
    SERVER_ERROR("1002","服务内部错误"),
    SIGN_ERROR("1003","数据签名错误"),
    OTHER_CHANNEL_USER("1004","其它渠道老用户"),
	PARAMETER_ERROR("1005","参数错误")
	;

    private String code;

    private String msg;

    RegisterGeiNiHuaEnums(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
