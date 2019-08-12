package com.nyd.user.model.enums;

/**
 * 
 * @author zhangdk
 *
 */
public enum RefundApplyCode {
	
    REFUND("ref","退款申请"),
    COMP("comp","投诉");

    private String code;

    private String msg;

    private RefundApplyCode(String code, String msg){
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
