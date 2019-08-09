package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.refund.RefundOrderPo;
import com.nyd.settlement.model.vo.refund.RefundOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author hwei
 * @create 2018-01-16 15:12
 **/
@Mapper
public interface RefundOrderStruct {
    RefundOrderStruct INSTANCE = Mappers.getMapper(RefundOrderStruct.class);

    @Mappings(value = {
            @Mapping(target = "fundCode",expression = "java(com.nyd.settlement.model.enums.FundCodeEnum.enumMap().get(po.getFundCode()))"),
            @Mapping(target = "testStatus",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(source = "payTime", target = "payTime", dateFormat = "yyyy-MM-dd")
    })
    RefundOrderVo po2Vo(RefundOrderPo po);

    PageInfo<RefundOrderVo> poPage2VoPage(PageInfo<RefundOrderPo> page);

}
