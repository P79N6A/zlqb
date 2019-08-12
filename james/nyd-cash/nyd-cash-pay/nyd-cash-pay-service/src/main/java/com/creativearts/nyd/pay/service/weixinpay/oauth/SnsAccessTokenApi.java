package com.creativearts.nyd.pay.service.weixinpay.oauth;

import com.creativearts.nyd.pay.config.utils.HttpUtils;
import com.creativearts.nyd.pay.config.utils.StrKit;
import com.creativearts.nyd.pay.service.weixinpay.PaymentKit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class SnsAccessTokenApi {
    private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
    private static String authorize_uri = "https://open.weixin.qq.com/connect/oauth2/authorize";
    private static String qrconnect_url = "https://open.weixin.qq.com/connect/qrconnect";

    public SnsAccessTokenApi() {
    }

    public static String getAuthorizeURL(String appId, String redirect_uri, boolean snsapiBase) {
        return getAuthorizeURL(appId, redirect_uri, (String)null, snsapiBase);
    }

    public static String getAuthorizeURL(String appId, String redirectUri, String state, boolean snsapiBase) {
        Map<String, String> params = new HashMap();
        params.put("appid", appId);
        params.put("response_type", "code");
        params.put("redirect_uri", redirectUri);
        if (snsapiBase) {
            params.put("scope", "snsapi_base");
        } else {
            params.put("scope", "snsapi_userinfo");
        }

        if (StrKit.isBlank(state)) {
            params.put("state", "wx#wechat_redirect");
        } else {
            params.put("state", state.concat("#wechat_redirect"));
        }

        String para = PaymentKit.packageSign(params, false);
        return authorize_uri + "?" + para;
    }

    public static String getQrConnectURL(String appId, String redirect_uri) {
        return getQrConnectURL(appId, redirect_uri, (String)null);
    }

    public static String getQrConnectURL(String appId, String redirect_uri, String state) {
        Map<String, String> params = new HashMap();
        params.put("appid", appId);
        params.put("response_type", "code");
        params.put("redirect_uri", redirect_uri);
        params.put("scope", "snsapi_login");
        if (StrKit.isBlank(state)) {
            params.put("state", "wx#wechat_redirect");
        } else {
            params.put("state", state.concat("#wechat_redirect"));
        }

        String para = PaymentKit.packageSign(params, false);
        return qrconnect_url + "?" + para;
    }

    public static SnsAccessToken getSnsAccessToken(String appId, String secret, String code) {
        final String accessTokenUrl = url.replace("{appid}", appId).replace("{secret}", secret).replace("{code}", code);
        return (SnsAccessToken)RetryUtils.retryOnException(3, new Callable<SnsAccessToken>() {
            public SnsAccessToken call() throws Exception {
                String json = HttpUtils.get(accessTokenUrl);
                return new SnsAccessToken(json);
            }
        });
    }
}
