package com.nyd.order.api.zzl;

import java.util.List;

import com.alibaba.fastjson.JSONObject;



public interface OrderSqlService<T> {

	/**
	 * 查询sql 
	 * @param 
	 */
	List<JSONObject> query(String sql);
	/**
	 * 查询sql 
	 * @param 
	 */
	JSONObject queryOne(String sql);
	/**
	 * 查询sql 转成class对象
	 * @param 
	 */
	List<T> queryT(String sql,Class clazz);
	/**
	 * 查询sql 
	 * @param 
	 */
	Long count(String sql);
	/**
	 * 查询sql 
	 * @param 
	 */
	List<JSONObject> page(String sql,int page,int size);
	/**
	 * 查询sql 
	 * @param 
	 */
	List<T> pageT(String sql, int page, int size,Class clazz);
	/**
	 * 删除sql
	 */
	boolean deleteSql(String sql);
	
	/**
	 * 增加sql
	 */
	Boolean insertSql(String sql);
	/**
	 * 更新sql
	 */
	Boolean updateSql(String sql);
	
	/**
	 * 求和sql 
	 * @param 
	 */
	Long sum(String sql);
}
