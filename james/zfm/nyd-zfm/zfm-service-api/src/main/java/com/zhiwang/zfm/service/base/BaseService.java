package com.zhiwang.zfm.service.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.zhiwang.zfm.common.page.BaseSearchForm;
import com.zhiwang.zfm.common.page.QueryResult;



/**
 * 服务接口的基类
 * 
 *
 * @param <T>此服务接口服务的数据模型，即model
 */
public interface BaseService<T> {
	/**
	 * 保存实体
	 * @param entity 欲保存的实体
	 * @throws Exception
	 */
	
	void save(T entity) throws Exception;
	
	/**
	 * 更新实体
	 * @param entity 实体id
	 */
	
	void update(T entity) throws Exception;
	
	/**
	 * 根据选择的更新实体
	 * @param entity
	 * @throws Exception
	 */
	
	void updateBySelective(T entity) throws Exception;
	
	/**
	 * 删除实体
	 * @param id
	 * @throws Exception
	 */
	void delete(Serializable id) throws Exception;
	
	/**
	 * 批量删除实体
	 * @param ids
	 * @throws Exception
	 */
	void batchDelete(Serializable []ids) throws Exception;
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T find(Serializable id) throws Exception;
	
	/**
	 * 获取数据
	 * @param form
	 * @return
	 * @return 根据查询条件查询的查询结果集
	 */
	public QueryResult<T> getData(BaseSearchForm form) throws Exception;
	
	/**
	 * 按条件查询记录总数
	 * @param form
	 * @return
	 * @throws Exception
	 */
	Long count(BaseSearchForm form) throws Exception;
	
	/**
	 * 按条件查询记录集合
	 * @param form
	 * @return
	 * @throws Exception
	 */
	/** 2015-11-14 注释
	 * List<T> findAll(BaseSearchForm form) throws Exception;
	 * */
	
	/**
	 * 函数功能说明 处理bootstrap 表格的 列 和字段
	 * zhuliang  2015-7-31 
	 * 修改内容 
	 * @param 
	 * @return   
	 * @throws 
	 */
	public List<Map<String, Object>> handleTableColumns(String[] title, String[] field);
}
