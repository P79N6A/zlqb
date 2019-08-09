package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.ReconResult;
import com.nyd.admin.model.dto.ReconResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Mapper
public interface ReconResultMapStruct {
    ReconResultMapStruct INSTANCE = Mappers.getMapper(ReconResultMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "contractStartTime", target = "contractStartTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndTime", target = "contractEndTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "reconciliationDay", target = "reconciliationDay", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "resultStatus",expression = "java(com.nyd.admin.model.enums.ReconciliationResultEnum.enumMap().get(po.getResultCode()))"),
            @Mapping(target = "fundName",expression = "java(com.nyd.admin.model.enums.FundSourceEnum.enumMap().get(po.getFundCode()))"),
    })
    ReconResultDto po2Dto(ReconResult po);



    PageInfo<ReconResultDto> poPage2DtoPage(PageInfo<ReconResult> page);
}
