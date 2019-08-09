package com.nyd.admin.dao;


import com.nyd.admin.entity.ChargeBackWsm;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface ChargebackWsmDao {
//    void save(ChargeBackWsm chargeBackWsm);
    void saveList(List<ChargeBackWsm> list, String tableName);
    void dropTable(String tableName);
    void createTable(String tableName);
}
