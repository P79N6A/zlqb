package com.nyd.application.model.common;


import java.io.Serializable;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 分页信息，分页查询时使用
 * @author fxw
 *
 */
@JsonIgnoreProperties
public class PageCommon implements Serializable{
	
    private static final Logger logger = LoggerFactory.getLogger(PageCommon.class);

	private static final long serialVersionUID = 3607626990775704759L;
	
	// 排序字段映射
	private LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
	
	private int pageNo = 1;
	private int pageSize  = PageConfig.DEFAULT.getDefaultPageSize();
	private int startIndex;
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if(pageNo <= 0 ){
			this.pageNo = 1;
		}else{
			this.pageNo = pageNo;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getStartIndex() {
		return startIndex == 0 ? (pageNo -1)*pageSize: startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public void setPageSize(int pageSize) {
		if(pageSize <=0 || pageSize > PageConfig.DEFAULT.getMaxPageSize()){
			logger.error("The pageSize param is too large, default page size will be used.");
			return;
		}
		this.pageSize = pageSize;
	}
	
	public void setPageSize(int pageSize, boolean forceSet) {
		if (forceSet)
			this.pageSize = pageSize;
		else
			setPageSize(pageSize);
	}
	
	public LinkedHashMap<String, String> getOrderby() {
		return orderby;
	}

	public void setOrderby(LinkedHashMap<String, String> orderby) {
		this.orderby = orderby;
	}

	// 排序
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
	
	
	public String getQueryCondition() {
		StringBuffer condition = new StringBuffer("");
		if (getPageSize() == 0) {
			return condition.toString();
		} else {
			condition.append(" limit ").append(getStartIndex()).append(",").append(pageSize);
		}
		return condition.toString();
	}

}
