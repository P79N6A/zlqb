package com.nyd.settlement.service.struct;

import com.nyd.settlement.model.po.repay.RepayBillPo;
import com.nyd.settlement.model.vo.repay.RepayBillVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by Dengw on 2018/1/19
 */
@Mapper
public interface BillStruct {
    BillStruct INSTANCE = Mappers.getMapper(BillStruct.class);

    @Mappings(value = {
            @Mapping(source = "promiseRepaymentDate", target = "promiseRepaymentDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "actualSettleDate", target = "actualSettleDate", dateFormat = "yyyy-MM-dd"),
    })
    RepayBillVo po2Vo(RepayBillPo po);

    List<RepayBillVo> poList2VoList(List<RepayBillPo> list);
}
