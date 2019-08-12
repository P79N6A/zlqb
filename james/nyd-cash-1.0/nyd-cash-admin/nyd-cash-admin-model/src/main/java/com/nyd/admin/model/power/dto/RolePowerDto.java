package com.nyd.admin.model.power.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hwei on 2018/1/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePowerDto implements Serializable {
    //角色Id
    private Integer id;
    //权限集合
    private List<Integer> powerList;
   
}
