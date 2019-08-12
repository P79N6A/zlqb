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
public class CallInfo implements Serializable {
    private String type;
    private String phoneNo;
    private String name;
    private String duration;
    private String calltime;
    private String deviceId;
    private String phoneOs;
    private String versionName;
    private String hour;
    private String callNo;
    private String appName;
    private String userId;
    private Date create_time = new Date();
}
