package com.nyd.order.service.consts;

/**
 * Created by Dengw on 2017/11/21
 */
public class OrderConsts {
    public static String DB_ERROR_MSG = "服务器开小差了,请稍后再试";
    public static String UNBUNDLED_BANK = "解绑银行卡失败";
    public static String REPAY_MSG = "存在未还清订单";
    public static String NO_PRODUCT = "无对应金融产品";
    public static String NO_PRAMA = "参数不能为空";
    public static String SMSCODE_ERROR = "验证码错误";
    public static String MEMBER_ERROR = "请您购买会员";
//    public static String PRODUCT_NOT_EXIST = "产品不存在";

    public static String REDIS_LOAN_KEY = "loanKey";
    public static String REDIS_LOAN_ACCOUNT_STATUS = "accountStatus";
    
    public static String AUDIT = "audit";
}
