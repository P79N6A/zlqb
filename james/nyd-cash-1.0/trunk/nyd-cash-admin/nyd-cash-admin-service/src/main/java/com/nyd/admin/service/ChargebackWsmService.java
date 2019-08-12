package com.nyd.admin.service;


import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.ChargebackWsmVo;

import java.text.ParseException;
import java.util.List;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
public interface ChargebackWsmService {
//    void save(ChargebackWsmVo chargebackWsmVo);
    void saveList(List<ChargebackWsmVo> chargebackWsmVos, String tableName);
    void dropTable(String tableName);
    void createTable(String tableName);
    PageInfo<ChargebackWsmVo> findPage(ChargebackWsmVo vo,String tableName) throws ParseException;
}
