package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.OverdueBill;
import com.nyd.settlement.entity.repay.RepayAdjustLog;
import com.nyd.settlement.model.dto.repay.RepayQueryDto;
import com.nyd.settlement.model.po.repay.RepayAmountOfDay;
import com.nyd.settlement.model.po.repay.RepayBillPo;
import com.nyd.settlement.model.po.repay.RepayPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Peng
 * @create 2018-01-18 11:52
 **/
@Mapper
public interface RepayMapper {
    List<RepayPo>repayRecordList(RepayQueryDto dto);

    List<RepayAmountOfDay> queryRepayAmountOfDay(@Param(value = "billNo") String billNo);

    List<RepayAmountOfDay> queryRepayAmountOfDayDistinct(@Param(value = "billNo") String billNo);

    OverdueBill queryOverdueBill(@Param(value = "billNo") String billNo);

    List<RepayBillPo> queryBillList(@Param(value = "orderNo") String orderNo);

    void insert(RepayPo po);

    void insertAdjustLog(RepayAdjustLog log);

}
