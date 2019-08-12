package com.nyd.user.service.consts;

/**
 * Created by Dengw on 2017/11/21
 */
public class UserConsts {
    public static String DB_ERROR_MSG = "服务器开小差了,请稍后再试";

    public static String PARAM_ERROR = "参数不能为空";

    public static String DB_ERROR = "数据库异常";

    public static String BANK_FOUR_ERROR = "银行卡验证未成功";

    public static String BANK_ALREADY_USE = "银行卡已被绑定";

    public static String BANK_MSG_ERROR = "短信验证码错误";

    public static String IDCARD_THREE_ERROR = "身份验证未成功";

    public static String IDCARD_ALREADY_REGISTER = "身份证已被注册，请更换";

    public static String CONTACT_FORMAT_ERROR = "联系人姓名格式不符，请更换";

    public static String AUTH_EXPIRED_MSG = "您的信用认证已到期请重新认证";
    
    public static String OPERATOR_OVERDUE ="2";//运营商认证过期

    //资料是否填写 1填写 0未填写
    public static String FILL_FLAG = "1";
    public static String UNFILL_FLAG = "0";
}
