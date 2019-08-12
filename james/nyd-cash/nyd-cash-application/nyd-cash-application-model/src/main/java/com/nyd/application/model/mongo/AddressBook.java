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
public class AddressBook implements Serializable {
    private String phoneNo;
    private String name;
    private String deviceId;
    private String phoneOs;
    private String versionName;
    private String tel;
    private String updatetime;
    private String appName;
    private String userId;
    private Date create_time = new Date();
}
