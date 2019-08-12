package com.nyd.application.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.nyd.application.api.ApplicationSqlService;
import com.nyd.application.dao.mapper.CallInfoMapper;
import com.nyd.application.model.common.ListTool;
import com.nyd.application.model.common.SqlSearchForm;


@Service
@Transactional
public class ApplicationSqlServiceImplrviceImpl<T> implements ApplicationSqlService<T> {
	
	@Resource
	private CallInfoMapper mapper;
	

	
	
	/**
	 * 查询sql 转成class对象
	 * @param 
	 */
	@Override
	public List<T> queryT(String sql,Class clazz){
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		List list =  mapper.sqlQuery(form);
		return new ListTool<T>().parseToObject(list, clazz);
	}
	@Override
	public List<JSONObject> query(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		List<JSONObject> list =  mapper.sqlQuery(form);
		if(list!=null && list.size()==1){
			JSONObject json = list.get(0);
			if(json == null){
				return new ArrayList<JSONObject>();
			}
		}
		return list;
	}
	@Override
	public JSONObject queryOne(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		List<JSONObject> list =   mapper.sqlQuery(form);
		if(list !=null && list.size()>0){
			return list.get(0);
		}
		return new JSONObject();
	}

	@Override
	public Long count(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		try {
			return mapper.sqlCount(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<JSONObject> page(String sql, int page, int size) {
		SqlSearchForm form = new SqlSearchForm();
		sql = sql + " limit "+(page-1)*size+","+size;
		form.setQueryValide(sql);
		try {
			return mapper.sqlPage(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<T> pageT(String sql, int page, int size,Class clazz) {
		SqlSearchForm form = new SqlSearchForm();
		sql = sql + " limit "+(page-1)*size+","+size;
		form.setQueryValide(sql);
		List list =  mapper.sqlQuery(form);
		return new ListTool<T>().parseToObject(list, clazz);
	}

	@Override
	public boolean deleteSql(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		try {
			mapper.deleteSql(form);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean insertSql(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		try {
			mapper.insertSql(form);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Boolean updateSql(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		try {
			mapper.updateSql(form);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Long sum(String sql) {
		SqlSearchForm form = new SqlSearchForm();
		form.setQueryValide(sql);
		try {
			return mapper.sqlSum(form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getInsertSql(String tableName,Object obj){
		return null;
	}
	
}
