package com.nyd.admin.model.enums;

/**
 * 退券类型
 * @author cm
 */
public enum RefundTicketType {
    RefundTicketType_one(1,"小银券"),
    RefundTicketType_Two(2,"其他类型的券");

    private Integer code;

    private String Desc;

    private RefundTicketType(Integer code, String desc) {
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
