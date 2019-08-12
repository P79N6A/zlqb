package com.tasfe.sis.auth.model.vo;

import lombok.Data;

/**
 * Created by Lait on 2017/7/28.
 */
@Data
public class UserVO {

    private String username;

    private String pwd;

    private Integer logintype;

    private Integer permissionSet;

}
