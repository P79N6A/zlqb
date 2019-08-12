package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.ChargeBackWsm;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Mapper
public interface ChargebackWsmMapper extends CrudDao<ChargeBackWsm> {
    void dropTable(String tableName);
    void createTable(String tableName);
    void insertBatchList(Map<String, Object> map);
}
