package com.zhiwang.zfm.dao.sys;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.page.BaseMapper;
import com.zhiwang.zfm.common.page.SqlSearchForm;
import com.zhiwang.zfm.common.response.bean.sys.CreditUserVo;
import com.zhiwang.zfm.entity.sys.query.RoleAndUserForm;
/**
 * User Mapper
 *
 */
public interface UserMapper<T> extends BaseMapper<T> {
	/**
	 * 获得角色-权限的相关信息
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	List<JSONObject> getRoleAndLogin(RoleAndUserForm aauForm);
	/**
	 * 通过催收公司获取催收专员
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	List<JSONObject> getUrgeEmpListByCompId(String compId);
	List<JSONObject>  getUserList();
	
    List<JSONObject> sqlQuery(SqlSearchForm form);
  	
  	Long sqlCount(SqlSearchForm form);
  	
  	List<JSONObject>  sqlPage(SqlSearchForm form);
  	
  	
  	void deleteSql(SqlSearchForm form);
  	
  
  	void insertSql(SqlSearchForm form);
  	
  	
  	void updateSql(SqlSearchForm form);
  	
  
  	Long sqlSum(SqlSearchForm form);
  	
  	List<CreditUserVo> getCreditUser();
}
