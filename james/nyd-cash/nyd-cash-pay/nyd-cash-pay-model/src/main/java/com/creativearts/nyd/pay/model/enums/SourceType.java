package com.creativearts.nyd.pay.model.enums;

/**
 * Cong Yuxiang
 * 2018/4/20
 **/
public enum SourceType {
    MEMBER_FEE("memFee"), // 银码头
    REPAY_NYD("repayNyd"),
    CASH_COUPON("cashCoupon"),//现金券
    KZJR_REPAY_YMT("kzjrRepayYmt");   //空中金融还款

//    CASH_COUPON("2");//现金券

    private String type;


    private SourceType(String type) {
        this.type = type;
    }

    /**
     * 来源
     * @return
     */
    public String getType(){
        return this.type;
    }

}
