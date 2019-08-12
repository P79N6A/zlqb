package com.nyd.order.model.common;

import java.util.LinkedHashMap;

/**
 * 功能说明：查询基类
 * @author 
 * Copyright 
 */
public class BaseSearchForm {

	private int page = 1; 

	private int rows; 

	private String beginTime;
	
	private String endTime;

	private LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 功能说明：getFirstResult 
	 * 获取记录的开始索引 	    
	 * weiyingni  2015-11-11
	 * 最后修改时间：2015-11-11
	 */
	public int getFirstResult() {
		if(this.page < 1){
			return 0;
		}
		return (this.page - 1) * this.rows;
	}
	
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setOrderby(LinkedHashMap<String, String> orderby) {
		this.orderby = orderby;
	}

	public LinkedHashMap<String, String> getOrderby() {
		return orderby;
	}

	public String getOrderCondition() {
		StringBuffer orderCondition = new StringBuffer("");
		// step1:判断orderby是否为空
		if (orderby.isEmpty()) {
			return orderCondition.toString();
		} else {
			// step2:遍历orderby装成排序
			orderCondition.append("order by ");
			for (String key : orderby.keySet()) {
				orderCondition.append(key).append(" ").append(orderby.get(key)).append(",");
			}
			orderCondition.deleteCharAt(orderCondition.length() - 1);
			return orderCondition.toString();
		}
	}

	/**
	 * 功能说明：getQueryCondition 修改append 内容	    
	 * weiyingni  2015-11-11
	 * 原本为page
	 */
	public String getQueryCondition() {
		StringBuffer condition = new StringBuffer("");
		if (getRows() == 0) {
			return condition.toString();
		} else {
			condition.append(" limit ").append(getFirstResult()).append(",").append(rows);
		}
		return condition.toString();
	}
}
