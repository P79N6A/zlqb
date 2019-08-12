package com.nyd.zeus.service;

import com.nyd.zeus.model.BillWsmVo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface BillWsmService {
//    void save(BillWsmVo billWsmVo);
    void saveList(List<BillWsmVo> billWsmVos,String tableName);
    void dropTable(String tableName);
    void createTable(String tableName);
//    void findBy()
}
