package com.nyd.user.model.enums;

/**
 * Created by Dengw on 2017/11/14
 */
public enum LoginCode {
    LOGIN_SUCESS("100","成功"),
    DB_ERROR("200","服务器暂时开了小差"),
    PASSWORD_ERROR("300","密码错误"),
    ACCOUNT_NOT_EXISTS("400","账号不存在"),
    ALREADY_LOGIN("500","已经登录");

    private String code;

    private String msg;

    private LoginCode(String code, String msg){
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
