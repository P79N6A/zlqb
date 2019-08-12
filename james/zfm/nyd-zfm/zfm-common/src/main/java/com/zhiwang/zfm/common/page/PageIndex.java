package com.zhiwang.zfm.common.page;

/**
 * 页面索引对象
 * 
 * @author gaoyufeng
 * 
 */
public class PageIndex {
	/**
	 * 开始索引
	 */
	private long startindex;

	/**
	 * 结束索引
	 */
	private long endindex;

	public PageIndex(long startindex, long endindex) {
		this.startindex = startindex;
		this.endindex = endindex;
	}

	public long getStartindex() {
		return startindex;
	}

	public void setStartindex(long startindex) {
		this.startindex = startindex;
	}

	public long getEndindex() {
		return endindex;
	}

	public void setEndindex(long endindex) {
		this.endindex = endindex;
	}

}
