package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.YmtPayFlowMapper;
import com.nyd.settlement.entity.repay.YmtPayFlow;
import com.nyd.settlement.service.YmtPayFlowService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YmtPayFlowServiceImpl implements YmtPayFlowService {

    @Autowired
    private YmtPayFlowMapper ymtPayFlowMapper;

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ZEUS)
    @Override
    public YmtPayFlow findByRepayNo(String repayNo) {
        return ymtPayFlowMapper.selectByRepayNo(repayNo);
    }

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ZEUS)
    @Override
    public YmtPayFlow findByTradeNo( String outTradeNo) {
        return ymtPayFlowMapper.selectByTradeNo(outTradeNo);
    }
}
