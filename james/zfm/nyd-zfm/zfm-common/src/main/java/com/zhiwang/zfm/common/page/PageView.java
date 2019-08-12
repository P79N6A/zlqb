package com.zhiwang.zfm.common.page;

import java.util.List;

/**
 * 页面视图
 * 
 * @author gaoyufeng
 * 
 * @param <T>
 *            此页面视图所维护的数据模型
 */
public class PageView<T> {
	/** 分页数据 **/
	private List<T> records;
	/** 页码开始索引和结束索引 **/
	private PageIndex pageindex;
	/** 总页数 **/
	private long totalpage = 1;
	/** 每页显示记录数 **/
	private int maxresult = 12;
	/** 当前页 **/
	private int currentpage = 1;
	/** 总记录数 **/
	private long totalrecord;
	/** 页码数量 **/
	private int pagecode = 10;

	/** 要获取记录的开始索引 **/
	public int getFirstResult() {
		return (this.currentpage - 1) * this.maxresult;
	}

	public int getPagecode() {
		return pagecode;
	}

	public void setPagecode(int pagecode) {
		this.pagecode = pagecode;
	}

	public PageView(int maxresult, int currentpage) {
		this.maxresult = maxresult;
		this.currentpage = currentpage;
	}

	public void setQueryResult(QueryResult<T> qr) {
		setTotalrecord(qr.getTotalrecord());
		setRecords(qr.getResultlist());
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
		setTotalpage(this.totalrecord % this.maxresult == 0 ? this.totalrecord / this.maxresult : this.totalrecord / this.maxresult + 1);
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public PageIndex getPageindex() {
		return pageindex;
	}

	public long getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(long totalpage) {
		this.totalpage = totalpage;
		this.pageindex = WebTool.getPageIndex(pagecode, currentpage, totalpage);
	}

	public int getMaxresult() {
		return maxresult;
	}
	

	public void setCurrentpage() {
		if(getCurrentpage()==1)
			this.currentpage=1;
		else
		{
		this.currentpage = getCurrentpage()-1;
		}
	}

	public int getCurrentpage() {
		return currentpage;
	}
}
