package com.nyd.admin.service;


import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.BillWsmVo;

import java.text.ParseException;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface BillWsmService {
//    void save(BillWsmVo billWsmVo);
    void saveList(List<BillWsmVo> billWsmVos, String tableName);
    void dropTable(String tableName);
    void createTable(String tableName);

    PageInfo<BillWsmVo> findPage(BillWsmVo vo,String tableName) throws ParseException;
//    void findBy()
}
