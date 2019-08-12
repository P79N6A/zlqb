package com.nyd.batch.dao.mapper;

import com.nyd.batch.entity.Bill;
import com.nyd.batch.entity.OverdueBill;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2018/1/3
 **/
@Repository
public interface CuiShouMapper {
    List<OverdueBill> getCuishouBills();
    List<OverdueBill> getOverdueStatusByTime(Map map);
    Bill getBillByBillNo(Map map);
    List<OverdueBill> getOverdueStatusYestoday();
    List<OverdueBill> getOverdueStatusAll(Map map);
    List<OverdueBill> getCuishouBillsAll();
}
