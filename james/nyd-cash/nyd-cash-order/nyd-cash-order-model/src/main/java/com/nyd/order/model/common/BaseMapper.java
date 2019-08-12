package com.nyd.order.model.common;

import java.io.Serializable;
import java.util.List;


/**
 * 映射器
 * @author 
 *
 * @param <T>
 */
public interface BaseMapper<T> {
	
	/**
	 * 插入一条记录
	 * @param entity
	 * @throws Exception
	 */
	void insert(T entity) throws Exception;
	
	/**
	 * 更新一条记录
	 * @param entity
	 * @throws Exception
	 */
	void update(T entity) throws Exception;
	
	/**
	 * 根据选择的更新记录
	 * @param entity
	 * @throws Exception
	 */
	void updateBySelective(T entity) throws Exception;
	
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	void delete(Serializable id) throws Exception;
	
	/**
	 * 批量删除
	 * @param ids
	 * @throws Exception
	 */
	void batchDelete(Serializable []ids) throws Exception;
	
	/**
	 * 依据id查询记录
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T selectById(Serializable id) throws Exception;
	
	/**
	 * 统计分页记录总数
	 * @param form
	 * @return
	 * @throws Exception
	 */
	Long pageTotalRecord(BaseSearchForm  form) throws Exception;
	/**
	 * 获取分页记录总数
	 * @param form
	 * @return
	 * @throws Exception
	 */
	List<T> pageData(BaseSearchForm  form) throws Exception;
	
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
	/** 2015-11-14注释
	  List<T> findAll(BaseSearchForm  form) throws Exception;
	*/
	
}
