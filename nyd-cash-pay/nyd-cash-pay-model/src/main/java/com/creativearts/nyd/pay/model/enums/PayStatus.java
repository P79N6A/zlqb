package com.creativearts.nyd.pay.model.enums;

/**
 * Cong Yuxiang
 * 2018/4/19
 **/
public enum PayStatus {
    PAYING("0"), // 银码头
    UN_PAY("1"),
    PAY_SUCESS("2"), // 侬要贷
	CJ_SUCESS("S"),
	CJ_RUNNING("P"),
	CJ_FAIL("F"),
	CJ_ID_TP("01");

    private String code;


    private PayStatus(String code) {
        this.code = code;
    }

    /**
     * 来源
     * @return
     */
    public String getCode(){
        return this.code;
    }

}
