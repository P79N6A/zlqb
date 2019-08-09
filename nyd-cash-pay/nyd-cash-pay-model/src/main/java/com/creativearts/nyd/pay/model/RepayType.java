package com.creativearts.nyd.pay.model;

/**
 * Cong Yuxiang
 * 2018/1/8
 **/
public enum RepayType {
    QK("0","强扣"),
    KJ("1","使用第二种方法还款"),
    ZD("2","用绑定的卡还款"),
    MFEE("3","会员费"),
    UNKNOW("10","非以上任何一种"),
    MFEE_FAIL("11","会员费失败"),
    QK_FAIL("12","强扣失败"),
    KJ_FAIL("13","使用第二种方式还款失败"),
    ZD_FAIL("14","用绑定的卡还款失败"),
    UNKNOW_FAIL("15","未知失败"),
    COUPON_FEE("16","充值现金券"),
    KZJR_REPAY("17","X空中金融还款"),
    BAOFOO_WITHHOLD("18","宝付代扣"),
	JOINPAY_WITHHOLD("19","汇聚代扣");
    private String code;
    private String message;
    private RepayType(String code,String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
