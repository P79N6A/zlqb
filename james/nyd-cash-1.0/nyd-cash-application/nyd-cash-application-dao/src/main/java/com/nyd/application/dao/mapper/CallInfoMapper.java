package com.nyd.application.dao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.model.call.CarryCalls;
import com.nyd.application.model.common.SqlSearchForm;

@Repository
public interface CallInfoMapper {

   	List<JSONObject> sqlQuery(SqlSearchForm form);
   	
   	Long sqlCount(SqlSearchForm form);
   	
   	List<JSONObject>  sqlPage(SqlSearchForm form);
   	
   	
   	void deleteSql(SqlSearchForm form);
   	
   
   	void insertSql(SqlSearchForm form);
   	
   	
   	void updateSql(SqlSearchForm form);
   	
   
   	Long sqlSum(SqlSearchForm form);
   	
   	Long insertSqlReturnId(CarryCalls form);

}
