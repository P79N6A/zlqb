package com.nyd.settlement.service;

import com.nyd.settlement.entity.YmtOrder;

import java.util.List;

public interface YmtOrderService {
    List<YmtOrder> findByRepayNo(String repayNo);
}
