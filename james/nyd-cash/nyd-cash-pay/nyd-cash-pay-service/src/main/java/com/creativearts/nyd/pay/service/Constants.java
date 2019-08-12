package com.creativearts.nyd.pay.service;

/**
 * Cong Yuxiang
 * 2018/1/9
 **/
public class Constants {
    public static final String HLB_QUICK_PAY="hlbk";
    public static final String QUICK_PAY="quickpay";

    public static final String PAY_PREFIX="nyd";
    //用于去重
    public static final String HLB_REDIS_PREFIX="hlbp";
    public static final String YSB_REDIS_PREFIX="ysbp";
    public static final String CJ_REDIS_PREFIX="cjp";

    //用于保留初始化信息 用于 回调
    public static final String HLB_NYD_CARRAY="hlbcarry";
    public static final String YSB_NYD_CARRAY="ysbcarry";

    //用于支付状态
    public static final String PAY_STATUS = "nydmemsta";

}
