package com.tasfe.zh.base.exception;

/**
 * Created by zhou on 17/6/27.
 */
@SuppressWarnings("serial")
public class JException extends Exception {

    public final static String OK = "00000000";
    public final static int Error_Unknow = 11111111;
    public final static int Error_CustomerNum = 00000002;
    public final static int Error_account = 00000003;

    private int code;

    public static String getMessage(int code) {
        switch (code) {
            case Error_Unknow:
                return "未知错误！";
            case Error_account:
                return "用户名或密码错误！";
            case Error_CustomerNum:
                return "客户号不存在！";

            default:
                return null; // cannot be
        }
    }

    public int getCode() {
        return code;
    }

//    JException(int code) {
//        super(getMessage(code));
//        this.code = code;
//    }

}
