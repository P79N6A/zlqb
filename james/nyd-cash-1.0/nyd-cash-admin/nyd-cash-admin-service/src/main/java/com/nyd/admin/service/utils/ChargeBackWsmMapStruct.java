package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.ChargeBackWsm;
import com.nyd.admin.model.ChargebackWsmVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Mapper
public interface ChargeBackWsmMapStruct {

    ChargeBackWsmMapStruct INSTANCE = Mappers.getMapper(ChargeBackWsmMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "contractStartTime", target = "contractStartTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndTime", target = "contractEndTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "reconciliationDay", target = "reconciliationDay", dateFormat = "yyyy-MM-dd")
    })
    ChargebackWsmVo po2Vo(ChargeBackWsm po);

    @Mappings(value = {
            @Mapping(source = "contractStartTime", target = "contractStartTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndTime", target = "contractEndTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "reconciliationDay", target = "reconciliationDay", dateFormat = "yyyy-MM-dd")
    })
    ChargeBackWsm vo2Po(ChargebackWsmVo vo);

    PageInfo<ChargebackWsmVo> poPage2VoPage(PageInfo<ChargeBackWsm> page);
}
