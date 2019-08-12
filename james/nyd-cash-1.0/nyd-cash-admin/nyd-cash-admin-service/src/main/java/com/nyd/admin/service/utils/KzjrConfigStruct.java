package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.KzjrProductConfig;
import com.nyd.admin.model.KzjrProductConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by Dengw on 2017/12/15
 */
@Mapper
public interface KzjrConfigStruct {
    KzjrConfigStruct INSTANCE = Mappers.getMapper(KzjrConfigStruct.class);

    @Mappings(value = {
            @Mapping(source = "useDate", target = "useDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "status",expression = "java(Integer.parseInt(vo.getStatus()))")
    })
    KzjrProductConfig vo2Po(KzjrProductConfigVo vo);

    @Mappings(value = {
            @Mapping(source = "useDate", target = "useDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "status",expression = "java(String.valueOf(po.getStatus()))")
    })
    KzjrProductConfigVo po2Vo(KzjrProductConfig po);

    PageInfo<KzjrProductConfigVo> poPage2VoPage(PageInfo<KzjrProductConfig> page);
}
