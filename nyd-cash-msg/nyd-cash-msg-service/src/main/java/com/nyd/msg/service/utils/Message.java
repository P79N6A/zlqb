package com.nyd.msg.service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
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


   /**
    * 第三方返回短信code
    */
    private String msgCode;
    /**
     * 第三方返回短信msgId
     */
    private String msgId;

    public Message() {
    }

    public Message(String smsPlatUrl, String smsPlatAccount, String smsPlatPwd, String smsTemplate, String cellPhones, String appName, String sign) {
        this.smsPlatUrl = smsPlatUrl;
        this.smsPlatAccount = smsPlatAccount;
        this.smsPlatPwd = smsPlatPwd;
        this.smsTemplate = smsTemplate;
        this.cellPhones = cellPhones;
        this.appName = appName;
        this.sign = sign;
    }
}
