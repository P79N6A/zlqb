package com.nyd.admin.model.power.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hwei on 2018/1/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleVo implements Serializable {
    private Long id;
    //角色名称
    private String roleName;
    //是否已删除
    private Integer deleteFlag;

}
