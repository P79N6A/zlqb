package com.nyd.user.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class HitRequest implements Serializable{
    //手机号
    private String mobile;

    //哪一家渠道
    private String source;

    //app名称
    private String appName;

    //手机号类型   1:普通手机号(未加密)   2：MD5加密手机号   3：sha加密手机号
    private String mobileType;
}
