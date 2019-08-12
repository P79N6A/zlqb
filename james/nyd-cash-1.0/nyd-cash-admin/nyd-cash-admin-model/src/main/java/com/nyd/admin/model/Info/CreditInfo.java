package com.nyd.admin.model.Info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: wucx
 * @Date: 2018/10/16 21:14
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditInfo implements Serializable {
    //姓名
    private String realName;
    //用户编号
    private String userId;
    //性别
    private String gender;
    //手机号
    private String accountNumber;
    //app名字
    private String appName;
    //注册时间
    private String createTime;
    //os平台
    private String os;
    //操作
    private String remark;
}
