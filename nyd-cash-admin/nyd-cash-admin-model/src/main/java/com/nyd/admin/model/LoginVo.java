package com.nyd.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Dengw on 2018/1/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginVo implements Serializable {
    private Long accountId;
    //账号
    private String accountNo;
    //登录成功token
    private String token;
}
