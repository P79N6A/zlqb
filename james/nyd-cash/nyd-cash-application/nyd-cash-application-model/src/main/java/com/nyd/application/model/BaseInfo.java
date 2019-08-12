package com.nyd.application.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by hwei on 2018/12/10.
 */
@Data
public class BaseInfo implements Serializable{
    // 账号
    private String accountNumber;
    // IOS使用
    private String userToken;
    // 设备Id
    private String deviceId;
}
