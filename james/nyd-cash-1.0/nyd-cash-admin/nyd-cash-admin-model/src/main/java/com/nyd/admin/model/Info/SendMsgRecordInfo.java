package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SendMsgRecordInfo implements Serializable {
    /**手机号*/
    private String mobile;
    private String appName;
    /**短信发送状态 0:未发送 1：发送失败 2：发送成功*/
    private Integer status;
    /**发送类型*/
    private Integer type;
    /**短信发送时间*/
    private Date sendSmsTime;
    /**手机认证时间*/
    private Date mobileTime;
}
