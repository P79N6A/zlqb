package com.nyd.zeus.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;


import com.nyd.zeus.model.common.SqlSearchForm;

import java.util.List;
import java.util.Map;

/**
 * Cong Yuxiang
 * 2017/11/22
 **/
@Mapper
public interface BillWsmMapper {
   void dropTable(String tablename);
   void createTable(String tablename);
   void insertBatchList(Map<String,Object> map);
   

  	List<JSONObject> sqlQuery(SqlSearchForm form);
  	
  	Long sqlCount(SqlSearchForm form);
  	
  	List<JSONObject>  sqlPage(SqlSearchForm form);
  	
  	
  	void deleteSql(SqlSearchForm form);
  	
  
  	void insertSql(SqlSearchForm form);
  	
  	
  	void updateSql(SqlSearchForm form);
  	
  
  	Long sqlSum(SqlSearchForm form);
}
