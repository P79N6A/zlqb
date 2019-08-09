package com.nyd.msg.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
public  class SmsRequest implements Serializable{
    /**
     * 表明sms类型 比如 1 注册 2 登录等
     */
    private Integer smsType;
    private String cellphone;
    private String sender;
    /**
     * app字母简称
     */
    private String appName;

    /**
     * 模板替换  对象需要实现tostring方法
     */
    private Map<String,Object> map;
    private String type;//是否是批量 type 1 批量  type 不为1是单个
    private String sign;//短信发送签名，短信迁移至技术中心之后使用字段
//    private Integer bizId;
}
