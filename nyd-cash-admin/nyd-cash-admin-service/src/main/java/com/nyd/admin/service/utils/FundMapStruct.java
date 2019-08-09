package com.nyd.admin.service.utils;

import com.nyd.admin.entity.Fund;
import com.nyd.admin.model.FundInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Mapper
public interface FundMapStruct {
    FundMapStruct INSTANCE = Mappers.getMapper(FundMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "remitStartTime", target = "remitStartTime", dateFormat = "HH:mm:ss"),
            @Mapping(source = "remitEndTime", target = "remitEndTime", dateFormat = "HH:mm:ss"),
            @Mapping(target = "isInUse",expression = "java(com.nyd.admin.model.enums.InUseEnum.enumMap().get(po.getIsInUse()))")
    })
    FundInfo po2Vo(Fund po);
    @Mappings(value = {
            @Mapping(source = "remitStartTime", target = "remitStartTime", dateFormat = "HH:mm:ss"),
            @Mapping(source = "remitEndTime", target = "remitEndTime", dateFormat = "HH:mm:ss")
    })
    Fund vo2Po(FundInfo vo);

    List<FundInfo> poList2VoList(List<Fund> polist);


}
