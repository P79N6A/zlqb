package com.nyd.admin.model;

import lombok.Data;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 18:22 2018/10/1
 */
@Data
public class UserSourceInfo {
    //账号号码
    private String accountNumber;
    //app名称
    private String appName;
    //用户来源
    private String source;
    /**
     * 手机系统类型
     */
    private String os;
}
