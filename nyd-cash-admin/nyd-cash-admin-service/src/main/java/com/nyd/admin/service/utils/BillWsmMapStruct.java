package com.nyd.admin.service.utils;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.entity.BillWsm;
import com.nyd.admin.model.BillWsmVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @describe:
 * @author: CongYuxiang
 */
@Mapper
public interface BillWsmMapStruct {
    BillWsmMapStruct INSTANCE = Mappers.getMapper(BillWsmMapStruct.class);

    @Mappings(value = {
            @Mapping(source = "contractStartTime", target = "contractStartTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndTime", target = "contractEndTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "reconciliationDay", target = "reconciliationDay", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "serviceCostPayday", target = "serviceCostPayday", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "payday", target = "payday", dateFormat = "yyyy-MM-dd")
    })
    BillWsmVo po2Vo(BillWsm po);
    @Mappings(value = {
            @Mapping(source = "contractStartTime", target = "contractStartTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "contractEndTime", target = "contractEndTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "reconciliationDay", target = "reconciliationDay", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "serviceCostPayday", target = "serviceCostPayday", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "payday", target = "payday", dateFormat = "yyyy-MM-dd")
    })
    BillWsm vo2Po(BillWsmVo vo);

    PageInfo<BillWsmVo> poPage2VoPage(PageInfo<BillWsm> page);

}
