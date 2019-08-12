package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.po.repay.RepayOrderPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Dengw on 2018/1/17
 */
@Mapper
public interface RepayOrderMapper {
    List<RepayOrderPo> repayOrderList(RepayQueryDto dto);
}
