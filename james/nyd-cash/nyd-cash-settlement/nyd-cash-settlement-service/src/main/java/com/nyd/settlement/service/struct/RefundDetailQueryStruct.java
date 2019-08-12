package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.YmtRefundPo;
import com.nyd.settlement.model.vo.YmtRefundVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RefundDetailQueryStruct {
    RefundDetailQueryStruct INSTANCE = Mappers.getMapper(RefundDetailQueryStruct.class);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(target = "label",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(source = "refundTime", target = "refundTime", dateFormat = "yyyy-MM-dd")
    })
    YmtRefundVo po2Vo(YmtRefundPo po);
    PageInfo<YmtRefundVo> poPage2VoPage(PageInfo<YmtRefundPo> page);
}
