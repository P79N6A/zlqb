package com.nyd.user.model.enums;

/**
 * Created by Dengw on 2017/11/14
 * 注册返回结果
 */
public enum RegisterCode {
    REGISTER_SUCESS("100","成功"),
    DB_ERROR("200","服务器暂时开了小差"),
    NOT_SUPPORT("210","不能提供服务"),

    SMSCODE_ERROR("300","短信验证码错误"),
    ACCOUNT_EXISTS("400","账号已存在"),
    PARAMETERS_ERROR("500","参数有误"),
	PHONE_ERROR("600","手机号格式有误");

    private String code;

    private String msg;

    private RegisterCode(String code, String msg){
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
