package com.creativearts.nyd.pay.service.weixinpay.oauth;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public interface IAccessTokenCache {
    int DEFAULT_TIME_OUT = 7195;

    String get(String var1);

    void set(String var1, String var2);

    void remove(String var1);
}
