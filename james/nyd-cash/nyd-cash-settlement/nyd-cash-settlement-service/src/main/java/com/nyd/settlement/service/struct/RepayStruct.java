package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.entity.repay.RepaySettleTmp;
import com.nyd.settlement.model.po.repay.RepayPo;
import com.nyd.settlement.model.vo.repay.RepayVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author Peng
 * @create 2018-01-18 19:30
 **/
@Mapper
public interface RepayStruct {

    RepayStruct INSTANCE = Mappers.getMapper(RepayStruct.class);


     @Mappings(value = {
     @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
     @Mapping(source = "repayTime", target = "repayTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
     @Mapping(target = "repayType",expression = "java(com.nyd.settlement.model.enums.RepayTypeEnum.enumMap().get(po.getRepayType()))"),
     @Mapping(target = "repayStatus",expression = "java(com.nyd.settlement.model.enums.RepayStatusEnum.enumMap().get(po.getRepayStatus()))"),
     @Mapping(target = "derateType",expression = "java(com.nyd.settlement.model.enums.DerateTypeEnum.enumMap().get(po.getDerateType()))")
     })
     RepayVo po2Vo(RepayPo po);

     PageInfo<RepayVo> poPage2VoPage(PageInfo<RepayPo> page);

 @Mappings(value = {
         @Mapping(source = "derateAmount", target = "derateAmountl")
 })
     RepayPo tmp2Po(RepaySettleTmp tmp);
}
