package com.nyd.batch.service;

import com.nyd.batch.entity.Bill;
import com.nyd.zeus.model.OverdueBillInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/20.
 */
public interface BillBatchService {

    void updateBillInfoAndOverdueBillInfo(Bill bill) throws Exception;

    void updateOverdueBillInfo(OverdueBillInfo overdueBillInfo);

    List<Bill> getSmsBills();
}
