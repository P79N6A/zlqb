package com.nyd.settlement.service.struct;

import com.nyd.settlement.entity.repay.TRepayLog;
import com.nyd.settlement.model.vo.repay.RepayDetailVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Cong Yuxiang
 * 2018/1/17
 **/
@Mapper
public interface RepayLogStruct {
    RepayLogStruct INSTANCE = Mappers.getMapper(RepayLogStruct.class);

    @Mappings(value = {
            @Mapping(target = "derateType",expression = "java(com.nyd.settlement.model.enums.DerateTypeEnum.enumMap().get(po.getDerateType()))"),
            @Mapping(source = "repayTime", target = "repayDateTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "updateBy", target = "operator")

    })
    RepayDetailVo po2Vo(TRepayLog po);

    List<RepayDetailVo> polist2Volist(List<TRepayLog> poList);

}
