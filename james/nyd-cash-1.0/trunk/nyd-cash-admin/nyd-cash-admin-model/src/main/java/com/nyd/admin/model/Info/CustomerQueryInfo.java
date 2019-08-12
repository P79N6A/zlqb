package com.nyd.admin.model.Info;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CustomerQueryInfo implements Serializable {

    //客户姓名
    private String realName;

    //客户编号
    private String userId;

    //手机号码
    private String accountNumber;

    //身份证号
    private String idNumber;

    //客户等级
    private String preAuditLevel;

    //APP名称
    private String appName;

    //用户来源
    private String source;

    //注册时间
    private Date createTime;


}
