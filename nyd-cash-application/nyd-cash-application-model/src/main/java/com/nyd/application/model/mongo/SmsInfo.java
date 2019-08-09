package com.nyd.application.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dengw on 2017/11/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmsInfo implements Serializable {
    private String phoneNo;
    private String deviceId;
    private String phoneOs;
    private String versionName;
    private String content;
    private String devicePhoneNo;
    private String callNo;
    private String time;
    private String type;
    private String appName;
    private String userId;
    private Date create_time = new Date();
}
