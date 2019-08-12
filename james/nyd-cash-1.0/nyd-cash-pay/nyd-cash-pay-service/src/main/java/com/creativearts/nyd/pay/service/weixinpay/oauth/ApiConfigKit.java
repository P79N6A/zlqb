package com.creativearts.nyd.pay.service.weixinpay.oauth;

import com.creativearts.nyd.pay.config.utils.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class ApiConfigKit {
    private static final Logger log = LoggerFactory.getLogger(ApiConfigKit.class);
    private static final ThreadLocal<String> TL = new ThreadLocal();
    private static final Map<String, ApiConfig> CFG_MAP = new ConcurrentHashMap();
    private static final String DEFAULT_CFG_KEY = "_default_cfg_key_";
    private static boolean devMode = false;
    static IAccessTokenCache accessTokenCache = new DefaultAccessTokenCache();

    public ApiConfigKit() {
    }

    public static void setDevMode(boolean devMode) {
        devMode = devMode;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    public static ApiConfig putApiConfig(ApiConfig apiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put("_default_cfg_key_", apiConfig);
        }

        return (ApiConfig)CFG_MAP.put(apiConfig.getAppId(), apiConfig);
    }

    public static ApiConfig removeApiConfig(ApiConfig apiConfig) {
        return removeApiConfig(apiConfig.getAppId());
    }

    public static ApiConfig removeApiConfig(String appId) {
        return (ApiConfig)CFG_MAP.remove(appId);
    }

    public static void setThreadLocalAppId(String appId) {
        if (StrKit.isBlank(appId)) {
            appId = ((ApiConfig)CFG_MAP.get("_default_cfg_key_")).getAppId();
        }

        TL.set(appId);
    }

    public static void removeThreadLocalAppId() {
        TL.remove();
    }

    public static String getAppId() {
        String appId = (String)TL.get();
        if (StrKit.isBlank(appId)) {
            appId = ((ApiConfig)CFG_MAP.get("_default_cfg_key_")).getAppId();
        }

        return appId;
    }

    public static ApiConfig getApiConfig() {
        String appId = getAppId();
        return getApiConfig(appId);
    }

    public static ApiConfig getApiConfig(String appId) {
        log.debug("appId: " + appId);
        ApiConfig cfg = (ApiConfig)CFG_MAP.get(appId);
        if (cfg == null) {
            throw new IllegalStateException("需事先调用 ApiConfigKit.putApiConfig(apiConfig) 将 appId对应的 ApiConfig 对象存入，如JFinalConfig.afterJFinalStart()中调用, 才可以使用 ApiConfigKit.getApiConfig() 系列方法");
        } else {
            return cfg;
        }
    }

    public static void setAccessTokenCache(IAccessTokenCache accessTokenCache) {
        accessTokenCache = accessTokenCache;
    }

    public static IAccessTokenCache getAccessTokenCache() {
        return accessTokenCache;
    }
}
