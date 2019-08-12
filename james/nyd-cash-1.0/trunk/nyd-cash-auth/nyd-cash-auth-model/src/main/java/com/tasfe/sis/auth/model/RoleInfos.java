package com.tasfe.sis.auth.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Lait on 2017/8/18.
 */
@Data
public class RoleInfos extends PageInfos {

    private Long id;

    // 角色的名称
    private String name;

    // 描述
    private String descr;

    // 状态
    private Integer status;

    //资源id
    private List<Long> resourcesIds;
}
