package com.nyd.admin.model.enums;

/**
 * 退券状态
 * @author cm
 */
public enum ReturnPremiumState {

    ReturnPremiumState_one(1,"已退费"),
    ReturnPremiumState_Two(2,"未退费"),
    ReturnPremiumState_Three(3,"退费失败");

    private Integer code;
    private String Desc;

    private ReturnPremiumState(Integer code, String desc) {
        this.code = code;
        Desc = desc;
    }

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getDesc() {
        return Desc;
    }
    public void setDesc(String desc) {
        Desc = desc;
    }
}
