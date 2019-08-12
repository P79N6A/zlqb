package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.entity.repay.YmtPayFlow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface YmtPayFlowMapper {
    YmtPayFlow selectByRepayNo(String repayNo);

    YmtPayFlow selectByTradeNo(String outTradeNo);
}
