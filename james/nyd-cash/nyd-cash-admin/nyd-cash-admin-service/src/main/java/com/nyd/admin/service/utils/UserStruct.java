package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.power.User;
import com.nyd.admin.model.power.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by zhujx on 2018/1/3.
 */
@Mapper
public interface UserStruct {
    UserStruct INSTANCE = Mappers.getMapper(UserStruct.class);

    UserVo Po2UserVo(User po);

    PageInfo<UserVo> poPage2VoPage(PageInfo<User> page);
}
