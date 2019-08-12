package com.nyd.admin.service.utils;

import com.nyd.admin.entity.RemitReport;
import com.nyd.admin.model.RemitReportVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Mapper
public interface RemitReportMapStruct {
    RemitReportMapStruct INSTANCE = Mappers.getMapper(RemitReportMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "contractStartDate", target = "contractStartDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndDate", target = "contractEndDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "memberFeePayDate", target = "memberFeePayDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    RemitReportVo po2Vo(RemitReport report);

    List<RemitReportVo> poList2VoList(List<RemitReport> list);
}
