package com.nyd.capital.model.enums;

public enum KdlcResponseEnum {

	NEW_ORDER_SUCCESS("0000","订单创建成功"),
    SERVER_ERROR("1001","服务内部错误"),
    SIGN_ERROR("1002","数据签名错误"),
	PARAMETER_ERROR("1003","参数错误");

    private String code;

    private String msg;

    KdlcResponseEnum(String code,String msg){
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
