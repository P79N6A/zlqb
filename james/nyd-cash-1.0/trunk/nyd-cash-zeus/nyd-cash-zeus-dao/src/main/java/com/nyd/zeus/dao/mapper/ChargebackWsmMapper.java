package com.nyd.zeus.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Mapper
public interface ChargebackWsmMapper {
    void dropTable(String tableName);
    void createTable(String tableName);
    void insertBatchList(Map<String,Object> map);
}
