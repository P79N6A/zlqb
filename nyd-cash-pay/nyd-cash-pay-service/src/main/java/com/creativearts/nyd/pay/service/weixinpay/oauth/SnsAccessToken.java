package com.creativearts.nyd.pay.service.weixinpay.oauth;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/15
 **/
public class SnsAccessToken implements RetryUtils.ResultCheck, Serializable {
    private static final long serialVersionUID = 6369625123403343963L;
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private Integer errcode;
    private String errmsg;
    private Long expiredTime;
    private String json;

    public SnsAccessToken(String jsonStr) {
        this.json = jsonStr;

        try {
            Map<String, Object> temp = (Map) JSON.parseObject(jsonStr, Map.class);
            this.access_token = (String)temp.get("access_token");
            this.expires_in = this.getInt(temp, "expires_in");
            this.refresh_token = (String)temp.get("refresh_token");
            this.openid = (String)temp.get("openid");
            this.unionid = (String)temp.get("unionid");
            this.scope = (String)temp.get("scope");
            this.errcode = this.getInt(temp, "errcode");
            this.errmsg = (String)temp.get("errmsg");
            if (this.expires_in != null) {
                this.expiredTime = System.currentTimeMillis() + (long)((this.expires_in.intValue() - 5) * 1000);
            }

        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public String getJson() {
        return this.json;
    }

    private Integer getInt(Map<String, Object> temp, String key) {
        Number number = (Number)temp.get(key);
        return number == null ? null : number.intValue();
    }

    public boolean isAvailable() {
        if (this.expiredTime == null) {
            return false;
        } else if (this.errcode != null) {
            return false;
        } else if (this.expiredTime.longValue() < System.currentTimeMillis()) {
            return false;
        } else {
            return this.access_token != null;
        }
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public Integer getExpiresIn() {
        return this.expires_in;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public String getOpenid() {
        return this.openid;
    }

    public String getScope() {
        return this.scope;
    }

    public Integer getErrorCode() {
        return this.errcode;
    }

    public String getErrorMsg() {
        if (this.errcode != null) {
//            String result = ReturnCode.get(this.errcode.intValue());
//            if (result != null) {
//                return result;
//            }
            return "error";
        }

        return this.errmsg;
    }

    public String getUnionid() {
        return this.unionid;
    }

    public boolean matching() {
        return this.isAvailable();
    }

}
