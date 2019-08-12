package com.nyd.zeus.dao;

import com.nyd.zeus.entity.BillWsm;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface BillWsmDao {
//    void save(BillWsm billWsm);
    void dropTable(String tableName);
    void createTable(String tableName);
    void saveList(List<BillWsm> list,String tableName);
//    int selectBy();
}
