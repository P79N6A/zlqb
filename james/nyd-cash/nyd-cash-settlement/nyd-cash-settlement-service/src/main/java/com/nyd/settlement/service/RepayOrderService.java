package com.nyd.settlement.service;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.vo.repay.RepayBillVo;
import com.nyd.settlement.model.vo.repay.RepayOrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Dengw on 2018/1/17
 */
@Mapper
public interface RepayOrderService {
    PageInfo<RepayOrderVo> findRepayPage (RepayQueryDto dto);

    List<RepayBillVo> getBillList(String orderNo);
}
