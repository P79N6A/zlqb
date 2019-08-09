package com.creativearts.nyd.pay.service.weixinpay.oauth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class DefaultAccessTokenCache implements IAccessTokenCache{
    private Map<String, String> map = new ConcurrentHashMap();

    public DefaultAccessTokenCache() {
    }

    public String get(String key) {
        return (String)this.map.get(key);
    }

    public void set(String key, String jsonValue) {
        this.map.put(key, jsonValue);
    }

    public void remove(String key) {
        this.map.remove(key);
    }
}
