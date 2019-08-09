package com.nyd.admin.model.power.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhujx on 2018/1/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRoleDto implements Serializable {

    //用户Id
    private Integer id;

    //角色Id集合
    private List<Integer> roleList;
}
