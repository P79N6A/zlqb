package com.nyd.order.model.dto;

import lombok.Data;

/**
 * Created by Dengw on 2017/11/20
 * 借款接口参数
 */
@Data
public class BorrowDto {
    //设备Id
    private String deviceId;
    //用户userId
    private String userId;
    //账号
    private String accountNumber;
    //金融产品code
    private String productCode;
    //渠道来源
    private String appName;
}
