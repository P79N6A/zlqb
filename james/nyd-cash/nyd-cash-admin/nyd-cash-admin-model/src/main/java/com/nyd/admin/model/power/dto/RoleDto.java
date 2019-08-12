package com.nyd.admin.model.power.dto;

import com.nyd.admin.model.paging.Paging;
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
public class RoleDto  extends Paging implements Serializable {
    //角色Id
    private Integer id;
    //角色名
    private String roleName;
    //是否已删除
    private Integer deleteFlag;
}
