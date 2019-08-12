package com.nyd.settlement.service;

import com.nyd.settlement.entity.repay.YmtPayFlow;

public interface YmtPayFlowService {
    YmtPayFlow findByRepayNo(String repayNo);

    YmtPayFlow findByTradeNo(String outTradeNo);
}
