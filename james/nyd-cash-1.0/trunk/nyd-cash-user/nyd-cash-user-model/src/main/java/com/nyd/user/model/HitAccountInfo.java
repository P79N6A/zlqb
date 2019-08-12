package com.nyd.user.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhangp
 * @Description:
 * @Date: 16:44 2018/9/5
 */
@Data
public class HitAccountInfo implements Serializable{
    //手机号
    private String accountNumber;
    //sha256加密后的字符串
    private String secretShaStr;
    //md5加密后的字符串
    private String secretMdStr;
    //其他方式
    private String secretOtherStr;
    //备注
    private String description;
}
