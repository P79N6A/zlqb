package com.nyd.application.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/12/8.
 */
@Data
@ToString
public class PushJiGuangInfo implements Serializable {
    private String providerId; //必填 1 极光
    private String appCode; //必填 ymt - 银码头，ddq - 哆哆钱等
    private String clientId; //设备ID
    private String deviceType;//设备类型 1 ios 2 android
    //一下为非必填参数
    private String userId;//用户id
    private String mobile;//用户手机号
    private String accountId;
    private String appVersion;
    private String deviceToken;
    private String deviceImei;
    private String deviceIdfa;
    private String deviceModel;//设备型号
    private String deviceSystem;//设备操作系统
    private String deviceMacAddr;//Mac地址
    private String deviceIpAddr;//IP地址
    private String longitude;//经度
    private String latitude;//维度



}
