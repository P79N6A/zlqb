package com.nyd.admin.model.power.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zhujx on 2018/1/3.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleRelVo implements Serializable {

    //用户id
    private Integer userId;
    //角色id
    private Integer roleId;

}
