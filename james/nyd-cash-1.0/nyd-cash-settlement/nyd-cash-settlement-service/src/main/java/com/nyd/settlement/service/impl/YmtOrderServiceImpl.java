package com.nyd.settlement.service.impl;

import com.nyd.settlement.dao.mapper.YmtOrderMapper;
import com.nyd.settlement.entity.YmtOrder;
import com.nyd.settlement.service.YmtOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YmtOrderServiceImpl implements YmtOrderService {

    @Autowired
    private YmtOrderMapper ymtOrderMapper;

    @Override
    public List<YmtOrder> findByRepayNo(String repayNo) {
        return ymtOrderMapper.selectByRepayNo(repayNo);
    }
}
