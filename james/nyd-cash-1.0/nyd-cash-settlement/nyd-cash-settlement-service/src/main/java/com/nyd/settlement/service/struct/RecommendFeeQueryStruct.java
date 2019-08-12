package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.po.RecommendFeePo;
import com.nyd.settlement.model.vo.RecommendFeeVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecommendFeeQueryStruct {

    RecommendFeeQueryStruct INSTANCE = Mappers.getMapper(RecommendFeeQueryStruct.class);

    @Mappings(value = {
            @Mapping(target = "productType",expression = "java(com.nyd.settlement.model.enums.ProductTypeEnum.enumMap().get(po.getProductType()))"),
            @Mapping(target = "label",expression = "java(com.nyd.settlement.model.enums.IsTestEnum.enumMap().get(po.getTestStatus()))"),
            @Mapping(target = "orderStatus", expression = "java(com.nyd.settlement.model.enums.OrderStatusEnum.enumMap().get(po.getOrderStatus()))"),
            @Mapping(target = "invitationCodeFlag", expression = "java(com.nyd.settlement.model.enums.InvitationCodeFlagEnum.enumMap().get(po.getInvitationCodeFlag()))"),
            @Mapping(source = "repayTime", target = "repayTime", dateFormat = "yyyy-MM-dd")
    })

    RecommendFeeVo po2Vo(RecommendFeePo po);
    PageInfo<RecommendFeeVo> poPage2VoPage(PageInfo<RecommendFeePo> page);


}
