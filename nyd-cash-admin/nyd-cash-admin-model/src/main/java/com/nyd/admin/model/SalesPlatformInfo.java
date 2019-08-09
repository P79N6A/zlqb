package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/30 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SalesPlatformInfo implements Serializable {

    //姓名
    private String realName;
    //性别
    private String gender;
    //用户编号
    private String userId;
    //手机号
    private String accountNumber;
    //注册时间
    private String createTime;
    //注册渠道
    private String appName;
    //手机系统
    private String os;
    //注册来源
    private String source;
    //最后一次登陆时间
    private String lastActiveTime;
    //客户等级
    private String preAuditLevel;
    //身份证号
    private String idNumber;
    //放款时间
    private String payTime;
    //是否认证
    private String identityFlag;
}
