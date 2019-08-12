package com.nyd.settlement.service.struct;

import com.nyd.settlement.entity.refund.YmtRefund;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecommendRefundAddStruct {
    RecommendRefundAddStruct INSTANCE = Mappers.getMapper(RecommendRefundAddStruct.class);

    @Mappings(value = {
            @Mapping(source = "refundStatus", target = "refundStatus"),
            @Mapping(source = "refundTime", target = "refundTime", dateFormat = "yyyy-MM-dd")
    })
    YmtRefund po2Vo(RecommendRefundDto po);
}
