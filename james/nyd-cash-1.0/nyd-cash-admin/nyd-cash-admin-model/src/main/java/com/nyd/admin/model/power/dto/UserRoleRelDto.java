package com.nyd.admin.model.power.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yifeng.qiu
 * @date 2018/6/5
 */
@Data
public class UserRoleRelDto implements Serializable {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 角色id
     */
    private Integer roleId;
}
