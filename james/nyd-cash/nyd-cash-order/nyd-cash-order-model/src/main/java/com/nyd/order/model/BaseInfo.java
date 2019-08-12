package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2017/11/18
 * 基本信息
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class BaseInfo implements Serializable{
    //设备Id
    private String deviceId;
    //账号
    private String accountNumber;
    //订单id
    private String orderNo;
    //用户ID
    private String userId;
    //fundCode
    private String fundCode;
    
    private String userToken;
    
    private String appName;
    private String reportKey;
}
