package com.nyd.user.dao.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.nyd.user.entity.UserBind;
import com.nyd.user.model.common.SqlSearchForm;
import com.nyd.user.model.vo.UserBankInfo;

@Repository
public interface UserBindMapper {
    void insert(UserBind user);

    List<UserBind> selectByUserIdAndCardNo(Map<String, String> params);
    
    List<UserBind> selectBindInfo(Map<String, String> params);
    
    void update(UserBind user);
    

   	List<JSONObject> sqlQuery(SqlSearchForm form);
   	
   	Long sqlCount(SqlSearchForm form);
   	
   	List<JSONObject>  sqlPage(SqlSearchForm form);
   	
   	
   	void deleteSql(SqlSearchForm form);
   	
   
   	void insertSql(SqlSearchForm form);
   	
   	
   	void updateSql(SqlSearchForm form);
   	
   
   	Long sqlSum(SqlSearchForm form);
   	
   	
   void	insertUserBankInfo(UserBankInfo vo);

}
