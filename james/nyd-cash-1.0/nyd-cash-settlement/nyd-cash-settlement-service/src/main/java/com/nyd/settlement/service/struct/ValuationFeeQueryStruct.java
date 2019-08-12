package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.ValuationFeePo;
import com.nyd.settlement.model.vo.ValuationFeeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ValuationFeeQueryStruct {

    ValuationFeeQueryStruct INSTANCE = Mappers.getMapper(ValuationFeeQueryStruct.class);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(target = "label",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(source = "repayTime", target = "repayTime", dateFormat = "yyyy-MM-dd")
    })
    ValuationFeeVo po2Vo(ValuationFeePo po);
    PageInfo<ValuationFeeVo> poPage2VoPage(PageInfo<ValuationFeePo> page);
}
