package com.nyd.zeus.dao;

import com.nyd.zeus.model.RepayInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/21.
 */
public interface RepayDao {

    void save(RepayInfo repayInfo) throws Exception;

    List<RepayInfo> getRepayInfoByBillNo(String billNo) throws Exception;
}
