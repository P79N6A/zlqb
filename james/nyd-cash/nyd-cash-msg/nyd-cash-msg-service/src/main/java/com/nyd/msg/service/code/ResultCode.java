package com.nyd.msg.service.code;

/**
 * 返回的代码
 */
public enum ResultCode {

    MSG_SUCESS("8000","成功"),
    MSG_SMS_THIRDPARTY_FAIL("8001","第三方短信平台失败"),
    MSG_SMS_PARAM_INCOMPLETE("8002","参数不全或为空"),
    MSG_SMS_BATCH_OVER_RANGE("8005","手机号超出范围"),
    MSG_OBJ_NULL("8003","检测对象为空"),
    MSG_OVER_TIMES("8004","发送验证码超过次数");


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
