package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.BillWsm;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Mapper
public interface BillWsmMapper extends CrudDao<BillWsm>{
   void dropTable(String tablename);
   void createTable(String tablename);
   void insertBatchList(Map<String, Object> map);

}
