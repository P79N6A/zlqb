package com.zhiwang.zfm.service.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhiwang.zfm.common.page.BaseMapper;
import com.zhiwang.zfm.common.page.BaseSearchForm;
import com.zhiwang.zfm.common.page.QueryResult;


/**
 * <p>服务支持<p>
 * 为服务组件的基类，必须继承
 * 
 * @author gaoyufeng
 * 
 * @param <T> 该服务组件服务的数据模型，即model;
 */
public abstract class ServiceSupport<T> implements BaseService<T> {
	
	
	private BaseMapper<T> mapper;
	
	public BaseMapper<T> getMapper() throws Exception{
		return mapper;
	}

	public void save(T entity) throws Exception {
		getMapper().insert(entity);
	}

	public void update(T entity) throws Exception{
		getMapper().update(entity);
	}

	public void delete(Serializable id) throws Exception {
		getMapper().delete(id);
	}

	public void batchDelete(Serializable[] ids) throws Exception {
		getMapper().batchDelete(ids);		
	}

	public T find(Serializable id) throws Exception {
		return getMapper().selectById(id);
	}

	public QueryResult<T> getData(BaseSearchForm form) throws Exception {
		QueryResult<T> qr = new QueryResult<T>();
		qr.setResultlist(getMapper().pageData(form));
		qr.setTotalrecord(getMapper().pageTotalRecord(form));
		return qr;
	}

	public void updateBySelective(T entity) throws Exception {
		getMapper().updateBySelective(entity);		
	}

	public Long count(BaseSearchForm form) throws Exception {
		return getMapper().count(form);
	}

	/**	2015-11-14 注释
	 * public List<T> findAll(BaseSearchForm form) throws Exception {
	 * return getMapper().findAll(form);
	}*/
	
	
	public List<Map<String, Object>> handleTableColumns(String[] title, String[] field)
	{
		List<Map<String, Object>> listColumns = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapColumns = null;
		
		for (int i = 0; i < title.length; i++)
		{
			mapColumns = new HashMap<String, Object>();
			mapColumns.put("title", title[i]);
			mapColumns.put("field", field[i]);
			listColumns.add(mapColumns);
		}

		return listColumns;
	}
	
}
