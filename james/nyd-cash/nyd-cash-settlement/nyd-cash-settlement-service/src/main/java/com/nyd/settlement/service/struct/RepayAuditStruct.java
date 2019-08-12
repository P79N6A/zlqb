package com.nyd.settlement.service.struct;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.entity.repay.RepaySettleTmp;
import com.nyd.settlement.model.dto.repay.RepayAddDto;
import com.nyd.settlement.model.vo.repay.RepayAuditVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
@Mapper
public interface RepayAuditStruct {
    RepayAuditStruct INSTANCE = Mappers.getMapper(RepayAuditStruct.class);

    @Mappings(value = {
            @Mapping(source = "transactionNo", target = "repayNo"),
            @Mapping(source = "repayDate", target = "repayTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "repayBankNo", target = "repayAccount"),
            @Mapping(source = "name", target = "userName")

    })
    RepaySettleTmp dto2Po(RepayAddDto dto);

    @Mappings(value = {
            @Mapping(source = "repayNo", target = "transactionNo"),
            @Mapping(source = "repayTime", target = "repayDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(source = "repayAccount", target = "repayBankNo"),
            @Mapping(target = "derateType",expression = "java(com.nyd.settlement.model.enums.DerateTypeEnum.enumMap().get(po.getDerateType()))")

    })
    RepayAuditVo po2Vo(RepaySettleTmp po);

   PageInfo<RepayAuditVo> poPage2VoPage(PageInfo<RepaySettleTmp> page);
}
