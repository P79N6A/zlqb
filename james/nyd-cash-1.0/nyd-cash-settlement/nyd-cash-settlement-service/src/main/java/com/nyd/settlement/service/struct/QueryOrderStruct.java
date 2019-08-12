package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.QueryOrderPo;
import com.nyd.settlement.model.vo.QueryOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
/**
 * @author Peng
 * @create 2018-01-16 15:12
 **/
@Mapper
public interface QueryOrderStruct {
    QueryOrderStruct INSTANCE = Mappers.getMapper(QueryOrderStruct.class);

    @Mappings(value = {
            @Mapping(source = "loanTime", target = "loanTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "payTime", target = "payTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "fundCode",expression = "java(com.nyd.settlement.model.enums.FundCodeEnum.enumMap().get(po.getFundCode()))"),
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(target = "testStatus",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))")
    })
    QueryOrderVo po2Vo(QueryOrderPo po);

    PageInfo<QueryOrderVo> poPage2VoPage(PageInfo<QueryOrderPo> page);

}
