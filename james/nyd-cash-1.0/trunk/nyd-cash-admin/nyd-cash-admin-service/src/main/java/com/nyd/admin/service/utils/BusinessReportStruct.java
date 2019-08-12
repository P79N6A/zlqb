package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.BusinessReport;
import com.nyd.admin.model.dto.BusinessReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessReportStruct {
    BusinessReportStruct NSTANCE = Mappers.getMapper(BusinessReportStruct.class);

    @Mappings(value = {
            @Mapping(source = "reportDate", target = "reportDate", dateFormat = "yyyy-MM-dd")
    })
    BusinessReportDto po2Dto(BusinessReport po);

    PageInfo<BusinessReportDto> poPage2VoPage(PageInfo<BusinessReport> page);
}
