package com.nyd.application.model.request;

import com.nyd.application.model.BaseInfo;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/12/10.
 */
@Data
@ToString
public class JiGuangDto  extends BaseInfo implements Serializable{
    private String appName;
    private String deviceType;
    private String userId;//用户id
    private String clientId;//极光id

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
