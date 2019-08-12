package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.power.dto.RoleDto;
import com.nyd.admin.model.power.dto.RolePowerDto;
import com.nyd.admin.model.power.dto.UserDto;
import com.nyd.admin.model.power.vo.RolePowerRelVo;
import com.nyd.admin.model.power.vo.RoleVo;
import com.nyd.admin.model.power.vo.UserVo;
import com.tasfe.framework.support.model.ResponseData;

import java.text.ParseException;
import java.util.List;

/**
 * Created by hwei on 2018/1/4.
 */
public interface RoleService {

    ResponseData saveRole(RoleDto roleDto);

    ResponseData updateRole(RoleDto roleDto);

    ResponseData findRoles(RoleDto roleDto);

    PageInfo<RoleVo> getRoleLs(RoleDto roleDto) throws ParseException;

    ResponseData findPowersByRole(RoleDto roleDto);

    List<RolePowerRelVo> findPowersByPowerId(Integer powerId);

    ResponseData editRolePower(RolePowerDto rolePowerDto);
}
