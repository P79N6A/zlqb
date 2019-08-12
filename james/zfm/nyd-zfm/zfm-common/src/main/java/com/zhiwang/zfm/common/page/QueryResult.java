package com.zhiwang.zfm.common.page;

import java.util.List;
/**
 * 查询结果
 * @author gaoyufeng
 *
 * @param <T>
 */
public class QueryResult<T> {
	
	/**
	 * 结果集
	 * */
	private List<T> resultlist;
	
	/**
	 * 
	 * 总数
	 * */
	private long totalrecord;
	
	public List<T> getResultlist() {
		return resultlist;
	}
	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}
	public long getTotalrecord() {
		return totalrecord;
	}
	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
	@Override
	public String toString() {
		return "QueryResult [resultlist=" + resultlist + ", totalrecord=" + totalrecord + "]";
	}
}
