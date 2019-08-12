package com.tasfe.zh.base.model.code;

/**
 * Created by zhou on 17/6/27.
 */
public enum ErrorCode {

    SYS_OK(1, "处理成功！"), SYS_EXCEPTION(ErrorCode.ERROR_CODE + 1, "系统错误！"),
    SYS_EXCEPTION_BEAN_COPY(ErrorCode.ERROR_CODE + 2, "系统错误！"),


    PARAMS_CustomerNum(ErrorCode.ERROR_CODE + 101, "客户号不能为空！"),
    PARAMS_LoginName(ErrorCode.ERROR_CODE + 102, "用户名不能为空！"),
    PARAMS_LoginPwd(ErrorCode.ERROR_CODE + 103, "密码不能为空！"),
    PARAMS_NULL(ErrorCode.ERROR_CODE + 104, "参数为空！"),


    PARAMS_DAYS_OUTOFRANGE(ErrorCode.ERROR_CODE + 104, "查询日期范围总天数超限！"),
    PARAMS_DATE_FORMAT_ERROR(ErrorCode.ERROR_CODE + 105, "日期格式不符【yyyy/MM/dd】！"),


    RESULT_CustomerNum(ErrorCode.ERROR_CODE + 201, "客户号不存在！"),
    RESULT_EXIST_ACCOUNT(ErrorCode.ERROR_CODE + 202, "用户名或密码错误！"),
    RESULT_NOTHING(ErrorCode.ERROR_CODE + 203, "无数据结果！"),
    RESULT_AUTH_CODE(ErrorCode.ERROR_CODE + 204, "授权码参数缺失！"),
    RESULT_TOKEN_EXPIRE(ErrorCode.ERROR_CODE + 205, "登录授权码已过期！"),;

    public static final int ERROR_CODE = 42000000;


    private Integer code;
    private String message;

    private ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer getCode() {
        // TODO Auto-generated method stub
        return code;
    }


    public String getMessage() {
        // TODO Auto-generated method stub
        return message;
    }


}
