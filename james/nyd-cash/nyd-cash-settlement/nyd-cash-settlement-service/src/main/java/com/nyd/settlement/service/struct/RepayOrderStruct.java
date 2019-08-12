package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.repay.RepayOrderPo;
import com.nyd.settlement.model.vo.repay.RepayOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by Dengw on 2018/1/17
 */
@Mapper
public interface RepayOrderStruct {
    RepayOrderStruct INSTANCE = Mappers.getMapper(RepayOrderStruct.class);

    @Mappings(value = {
            @Mapping(target = "capitalName",expression = "java(com.nyd.settlement.model.enums.FundCodeEnum.enumMap().get(po.getFundCode()))"),
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(target = "label",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(source = "payTime", target = "remitDate", dateFormat = "yyyy-MM-dd")
    })
    RepayOrderVo po2Vo(RepayOrderPo po);

    PageInfo<RepayOrderVo> poPage2VoPage(PageInfo<RepayOrderPo> page);
}
