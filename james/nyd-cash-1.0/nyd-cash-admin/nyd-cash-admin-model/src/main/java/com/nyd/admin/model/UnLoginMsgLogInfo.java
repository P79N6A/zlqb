package com.nyd.admin.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 14:11 2018/10/1
 */
@Data
public class UnLoginMsgLogInfo implements Serializable {
    /**手机号*/
    private String mobile;
    /**ymt或马甲*/
    private String appName;
    /**短信状态*/
    private Integer status;
    /**发送短信类型 0:未登录发送短信*/
    private Integer type;
    /**发送次数*/
    private Integer sendCount;
    /**短信发送时间*/
    private Date sendSmsTime;
}
