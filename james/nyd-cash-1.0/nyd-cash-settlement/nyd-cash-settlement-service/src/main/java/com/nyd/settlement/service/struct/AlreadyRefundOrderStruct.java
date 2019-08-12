package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.refund.AlreadyRefundOrderPo;
import com.nyd.settlement.model.vo.refund.AlreadyRefundOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author hwei
 * @create 2018-01-18 15:12
 **/
@Mapper
public interface AlreadyRefundOrderStruct {
    AlreadyRefundOrderStruct INSTANCE = Mappers.getMapper(AlreadyRefundOrderStruct.class);

    @Mappings(value = {
            @Mapping(target = "fundCode",expression = "java(com.nyd.settlement.model.enums.FundCodeEnum.enumMap().get(po.getFundCode()))"),
            @Mapping(target = "testStatus",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(source = "payTime", target = "payTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "repayTime", target = "repayTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "refundTime", target = "refundTime", dateFormat = "yyyy-MM-dd")
    })
    AlreadyRefundOrderVo po2Vo(AlreadyRefundOrderPo po);

    PageInfo<AlreadyRefundOrderVo> poPage2VoPage(PageInfo<AlreadyRefundOrderPo> page);

}
