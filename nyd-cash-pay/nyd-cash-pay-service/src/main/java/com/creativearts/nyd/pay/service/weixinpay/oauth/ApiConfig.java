package com.creativearts.nyd.pay.service.weixinpay.oauth;

import java.io.Serializable;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class ApiConfig implements Serializable{
    private String token = null;
    private String appId = null;
    private String appSecret = null;
    private String encodingAesKey = null;
    private boolean messageEncrypt = false;

    public ApiConfig() {
    }

    public ApiConfig(String token) {
        this.setToken(token);
    }

    public ApiConfig(String token, String appId, String appSecret) {
        this.setToken(token);
        this.setAppId(appId);
        this.setAppSecret(appSecret);
    }

    public ApiConfig(String token, String appId, String appSecret, boolean messageEncrypt, String encodingAesKey) {
        this.setToken(token);
        this.setAppId(appId);
        this.setAppSecret(appSecret);
        this.setEncryptMessage(messageEncrypt);
        this.setEncodingAesKey(encodingAesKey);
    }

    public String getToken() {
        if (this.token == null) {
            throw new IllegalStateException("token 未被赋值");
        } else {
            return this.token;
        }
    }

    public void setToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("token 值不能为 null");
        } else {
            this.token = token;
        }
    }

    public String getAppId() {
        if (this.appId == null) {
            throw new IllegalStateException("appId 未被赋值");
        } else {
            return this.appId;
        }
    }

    public void setAppId(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException("appId 值不能为 null");
        } else {
            this.appId = appId;
        }
    }

    public String getAppSecret() {
        if (this.appSecret == null) {
            throw new IllegalStateException("appSecret 未被赋值");
        } else {
            return this.appSecret;
        }
    }

    public void setAppSecret(String appSecret) {
        if (appSecret == null) {
            throw new IllegalArgumentException("appSecret 值不能为 null");
        } else {
            this.appSecret = appSecret;
        }
    }

    public String getEncodingAesKey() {
        if (this.encodingAesKey == null) {
            throw new IllegalStateException("encodingAesKey 未被赋值");
        } else {
            return this.encodingAesKey;
        }
    }

    public void setEncodingAesKey(String encodingAesKey) {
        if (encodingAesKey == null) {
            throw new IllegalArgumentException("encodingAesKey 值不能为 null");
        } else {
            this.encodingAesKey = encodingAesKey;
        }
    }

    public boolean isEncryptMessage() {
        return this.messageEncrypt;
    }

    public void setEncryptMessage(boolean messageEncrypt) {
        this.messageEncrypt = messageEncrypt;
    }
}
