package com.nyd.batch.dao.mapper;

import com.nyd.batch.entity.AmountOfHistory;
import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.MaturityBill;
import com.nyd.batch.entity.OverdueBill;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zhujx on 2017/11/20.
 */
@Repository
public interface BillMapper {

    List<Bill> getBillLs(Map map);

    List<OverdueBill> getOverdueBillLs(Map map);

    List<MaturityBill> getMaturityBills(Map map);

    List<MaturityBill> getMaturityBill(Map map);

    List<Bill> getBillByOrderNo(Map map);
    List<Bill> getBillByBillNo(Map map);

    OverdueBill getOverDueBillByBillNo(Map map);
//    void updateBill(Map map);
    List<Bill> getSmsBills();

    List<Bill> getBillsRepayOnTheDay(Map map);

    //获取当前账单历史还款记录
    AmountOfHistory queryRepayAmountHistory(Map map);

}
