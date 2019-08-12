package com.nyd.capital.service.jx.config;

/**
 * @author liuqiu
 */
public class OpenPageConstant {


    /**
     * 错误码 0 正常 1 失败
     */
    public static final String STATUS_ONE = "1";
    public static final String STATUS_ZERO = "0";

    /**
     * 即信推单外审结果 3 通过 4 不通过
     */
    public static final int PUSH_AUDIT_PASS = 3;
    public static final int PUSH_AUDIT_FAIL = 4;

    /**
     * 开户进程为实名认证
     */
    public static final String OPEN_PAGE_STAGE_IDCARD= "1";
    /**
     * 开户进程为交易密码
     */
    public static final String OPEN_PAGE_STAGE_TRADE= "2";
    /**
     * 开户进程为缴费授权
     */
    public static final String OPEN_PAGE_STAGE_PAYMENT= "3";
    /**
     * 开户进程为还款授权
     */
    public static final String OPEN_PAGE_STAGE_REPAYMENT= "4";
    /**
     * 开户进程为电子签章
     */
    public static final String OPEN_PAGE_STAGE_ACCREDIT= "5";


    public static final String OPEN_PAGE_LEFT= "left";


    /**
     * 轮询后该用户开户结果为已开户
     */
    public static final String QUERY_RESULT_HAS_OPEN = "4";
    /**
     * 轮询后该用户开户结果失败
     */
    public static final String QUERY_RESULT_FAIL = "5";
    /**
     * 轮询后还在开户中
     */
    public static final String QUERY_RESULT_OPENING = "6";
    /**
     *参数错误
     */
    public static final String PRARM_ERROR = "参数错误";

    /**
     *调用dubbo服务出错
     */
    public static final String DUBBO_ERROR = "调用dubbo服务出错";
    public static final String NULL_DATA = "空数据";
    public static final String NULL_ORDERNO = "订单号为空";
    public static final String NULL_CAPITAL = "资产渠道为空";
    public static final String NULL_USERID = "用户id为空";
    public static final String NULL_BANK = "银行卡号为空";
    public static final String STATUS_ERROR = "错误的状态码";

    public static final String OPEM_PAGE_REDIS_CODE = "jx:html:code";
    public static final String OPEM_PAGE_USER_INFO = "jx:html:info";
    public static final String OPEM_PAGE_REDIS_KZJR_RETURN = "kzjrReturn";
    public static final Object OPEM_PAGE_REDIS_RESULT = "jiangxibank:html:result";
    public static final Object OPEM_PAGE_REDIS_KEY = "jiangxibank:html:key";

    public static final String REDIS_MISS = "redis缓存丢失";


    public static final String OPEM_PAGE_FOR = "jx:openPage:key";
    public static final String POCKET_GETCODE = "pocket:getCode";
    public static final String OPEM_PAGE_BANK = "jx:openPage:bank";

    public static final String INPUT = "input";
    public static final String MEN = "男";
    public static final String WOMEN = "女";
    public static final String OPEN_PAGE_REDIS_URL = "pocket:html:url";
    public static final String ERROR_ORDER = "pocket:openPage:errorOrder";
    public static final String OPEM_PAGE_ING = "pocket:opening";
    public static final String NEW_POCKET_CALLBACK_LOAN = "pocket:callback:loan";
    public static final String NEW_POCKET_CALLBACK_WITHDRAW = "pocket:callback:withdraw";
    public static final String NEW_POCKET_DEPOSITORY = "pocket:depository:lines";
}
