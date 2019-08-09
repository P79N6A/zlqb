package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.FailReport;
import com.nyd.admin.model.dto.FailReportDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
/**
 * @author Peng
 * @create 2017-12-14 22:30
 **/
@Mapper
public interface FailReportStruct {
    FailReportStruct NSTANCE = Mappers.getMapper(FailReportStruct.class);

    @Mappings(value = {
            @Mapping(source = "reportDate", target = "reportDate", dateFormat = "yyyy-MM-dd")
    })
    FailReportDto po2Dto(FailReport po);

    PageInfo<FailReportDto> poPage2VoPage(PageInfo<FailReport> page);
}
