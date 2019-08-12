package com.nyd.msg.service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public  class Message implements Serializable{
    private String smsPlatUrl;
    private String smsPlatAccount;
    private String smsPlatPwd;
   private String smsTemplate;
    /**
     * 多手机 用 ;相隔
     */
   private String cellPhones;
   private String appName;
   private String sign;
}
