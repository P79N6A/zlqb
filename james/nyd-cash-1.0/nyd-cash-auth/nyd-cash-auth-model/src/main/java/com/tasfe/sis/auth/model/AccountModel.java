package com.tasfe.sis.auth.model;

import com.tasfe.sis.auth.entity.Role;
import lombok.Data;

import java.util.List;

/**
 * Created by hefusang on 2017/8/23.
 */
@Data
public class AccountModel {

    private Long id;
    // 关联id 可关联的是一个用户的id,一个企业账户的id等
    private Long rid;
    // 登陆名可以是手机号/自定义账户/第三方关联账户
    private String account;
    // 密码
    private String pwd;
    // 昵称
    private String nick;
    // 状态
    private Integer status;
    // 类型
    private Integer typ;

    private List<Role> roles;

}
