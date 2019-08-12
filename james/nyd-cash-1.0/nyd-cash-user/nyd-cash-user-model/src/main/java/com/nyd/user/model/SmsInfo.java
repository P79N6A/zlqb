package com.nyd.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Dengw on 2017/11/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SmsInfo {
    //手机号
    private String mobile;
    //发送模板
    private String smsType;
    /**
     * app名称
     */
    private String appName;
    private String sole;
    private String deviceId;


}
