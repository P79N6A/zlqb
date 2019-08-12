package com.nyd.admin.dao;

import com.nyd.admin.model.power.dto.RoleDto;
import com.nyd.admin.model.power.vo.RolePowerRelVo;
import com.nyd.admin.model.power.vo.RoleVo;

import java.util.List;

/**
 * Created by hwei on 2018/1/4.
 */
public interface RoleDao {
    void saveRole(RoleDto roleDto) throws Exception;

    void updateRole(RoleDto roleDto) throws Exception;

    List<RoleVo> findRoles(RoleDto roleDto) throws Exception;

    List<RolePowerRelVo> findPowersByRole(RoleDto roleDto) throws Exception;

    List<RolePowerRelVo> findPowersByPowerId(Integer powerId)throws Exception;

    void deletePowersByRoleId(Integer roleId) throws Exception;
}
