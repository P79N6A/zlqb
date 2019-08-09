package com.nyd.admin.model.power.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/1/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePowerRelVo implements Serializable {

    //角色id
    private Integer roleId;
    //权限id
    private Integer powerId;

}
