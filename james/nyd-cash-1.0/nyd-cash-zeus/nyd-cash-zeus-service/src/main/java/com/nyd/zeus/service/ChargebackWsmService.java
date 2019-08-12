package com.nyd.zeus.service;

import com.nyd.zeus.model.ChargebackWsmVo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface ChargebackWsmService {
//    void save(ChargebackWsmVo chargebackWsmVo);
    void saveList(List<ChargebackWsmVo> chargebackWsmVos,String tableName);
    void dropTable(String tableName);
    void createTable(String tableName);
}
