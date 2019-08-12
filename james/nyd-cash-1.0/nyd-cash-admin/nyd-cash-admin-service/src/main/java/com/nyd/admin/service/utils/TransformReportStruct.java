package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.TransformReport;
import com.nyd.admin.model.dto.TransformReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransformReportStruct {
    TransformReportStruct NSTANCE = Mappers.getMapper(TransformReportStruct.class);

    @Mappings(value = {
            @Mapping(source = "reportDate", target = "reportDate", dateFormat = "yyyy-MM-dd"),
    })
    TransformReportDto po2Dto(TransformReport po);

    PageInfo<TransformReportDto> poPage2VoPage(PageInfo<TransformReport> page);
}
