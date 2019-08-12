package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.power.Role;
import com.nyd.admin.model.power.vo.RoleVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by zhujx on 2018/1/6.
 */
@Mapper
public interface RoleStruct {

    RoleStruct INSTANCE = Mappers.getMapper(RoleStruct.class);

    RoleVo Po2RoleVo(Role po);

    PageInfo<RoleVo> poPage2VoPage(PageInfo<Role> page);
}
