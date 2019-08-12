package com.nyd.zeus.dao;

import com.nyd.zeus.model.BillInfo;
import com.nyd.zeus.model.OverdueBillInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
public interface OverdueBillDao {

    void save(OverdueBillInfo overdueBillInfo) throws Exception;

    void update(OverdueBillInfo overdueBillInfo) throws Exception;

    List<OverdueBillInfo> getObjectsByOrderNo(String orderNo) throws Exception;

    List<OverdueBillInfo> getObjectsByUserId(String userId) throws Exception;

    OverdueBillInfo getObjectByBillNo(String billNo)throws Exception;
    /**
     * 根据用户id查找最新的逾期记录
     * @param userId
     * @return
     * @throws Exception
     */
    OverdueBillInfo getObjectByUserId(String userId)throws Exception;

}
