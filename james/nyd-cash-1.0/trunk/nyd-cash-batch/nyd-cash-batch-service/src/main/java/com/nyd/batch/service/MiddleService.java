package com.nyd.batch.service;

import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.FriendCircle;
import com.nyd.batch.entity.OverdueBill;

import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/30
 **/
public interface MiddleService {
    FriendCircle selectByMobile(String mobile);

    List<OverdueBill> getCuishouBills();
    List<OverdueBill> getCuishouBillsAll();

    Bill getBillByBillNo(Map map);

    void selectTestAop();
    void selectInnerTestAop();
}
