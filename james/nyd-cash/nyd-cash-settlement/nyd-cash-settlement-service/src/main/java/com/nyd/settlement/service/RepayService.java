package com.nyd.settlement.service;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.dto.repay.RepayAdviceDto;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.po.repay.RepayPo;
import com.nyd.settlement.model.vo.repay.RepayDetailVo;
import com.nyd.settlement.model.vo.repay.RepayVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Cong Yuxiang
 * 2018/1/16
 **/
public interface RepayService {
    List<RepayDetailVo> queryRepayDetailByBillNo(String billNo);

    BigDecimal adviceAmount(RepayAdviceDto dto) throws Exception;

    PageInfo<RepayVo> findPage(RepayQueryDto dto);

    void test(RepayPo po);
}
