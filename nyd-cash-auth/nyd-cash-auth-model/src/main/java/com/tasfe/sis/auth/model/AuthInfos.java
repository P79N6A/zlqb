package com.tasfe.sis.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Lait on 2017/7/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthInfos {

    // 账户ID account id
    private Long aid;

    // 角色ID role id
    private Long rid;

    // 登陆名（手机号？）
    private String account;

    //密码
    private String pwd;

    // 版本？
    private String version;

    // session code
    private String sessionCode;
}
