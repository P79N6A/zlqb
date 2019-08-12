package com.nyd.user.model.enums;

/**
 * Created by Dengw on 2017/11/14
 * 修改密码或忘记密码返回结果
 */
public enum PasswordCode {
    MODIFY_SUCESS("100","成功"),
    DB_ERROR("200","服务器暂时开了小差"),
    SMSCODE_ERROR("300","短信验证码错误"),
    ACCOUNT_NOT_EXISTS("400","账号不存在"),
    PASSWORD_ERROR("500","原密码错误");

    private String code;

    private String msg;

    private PasswordCode(String code, String msg){
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
