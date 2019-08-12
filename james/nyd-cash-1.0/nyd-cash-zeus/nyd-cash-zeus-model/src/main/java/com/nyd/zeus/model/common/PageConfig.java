package com.nyd.zeus.model.common;

/**
 * 分页配置，最大条数、每页多少条
 * @author fxw
 *
 */
public final class PageConfig {

	private static final int MAX_PAGE_SIZE = 10000;
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	public static final PageConfig DEFAULT = new PageConfig();
	
	private PageConfig(){
		
	}
	
	private int defaultPageSize = DEFAULT_PAGE_SIZE;
	
	private int maxPageSize = MAX_PAGE_SIZE;

	public int getDefaultPageSize() {
		return defaultPageSize;
	}

	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

	public int getMaxPageSize() {
		return maxPageSize;
	}

	public void setMaxPageSize(int maxPageSize) {
		this.maxPageSize = maxPageSize;
	}
	
}
