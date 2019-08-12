package com.nyd.zeus.dao;

import com.nyd.zeus.entity.BillProduct;
import com.nyd.zeus.entity.BillRepay;
import com.nyd.zeus.model.BillInfo;

import java.util.List;

/**
 * Created by zhujx on 2017/11/18.
 */
public interface BillDao {

    void save(BillInfo billInfo) throws Exception;

    void update(BillInfo billInfo) throws Exception;

    List<BillInfo> getObjectsByOrderNo(String orderNo) throws Exception;

    List<BillInfo> getObjectsByYmtOrderNo(String ibankOrderNo) throws Exception;

    List<BillInfo> getObjectsByBillNo(String billInfo) throws Exception;

    List<BillInfo> getObjectsByUserId(String userId) throws Exception;

    BillInfo getObjectByUserId(String userId) throws Exception;
    
    void saveBillProduct(BillProduct product) throws Exception;
    
    void saveBillRepay(BillRepay repay);
    
  
}
