package com.creativearts.nyd.pay.model;

import lombok.Data;

@Data
public class Constant {

    //会员费
    public static  final String MEMBER_TYPE = "1";

    //快捷支付
    public static  final String KPAY_TYPE = "2";

    //主动还款
    public static  final String ZPAY_TYPE = "3";

    //强扣
    public static  final String QPAY_TYPE = "4";

    //充值现金券
    public static  final String COUPON_TYPE = "5";


}
