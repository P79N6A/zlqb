package com.nyd.admin.service.utils;

import com.nyd.admin.entity.RepayReport;
import com.nyd.admin.model.RepayReportVo;
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
public interface RepayReportMapStruct {
    RepayReportMapStruct INSTANCE = Mappers.getMapper(RepayReportMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "contractStartDate", target = "contractStartDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndDate", target = "contractEndDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "actualReceiptDay", target = "actualReceiptDay", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "memberFeePayDate", target = "memberFeePayDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "expireTime", target = "expireTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    RepayReportVo po2Vo(RepayReport report);

    List<RepayReportVo> poList2VoList(List<RepayReport> list);
}
