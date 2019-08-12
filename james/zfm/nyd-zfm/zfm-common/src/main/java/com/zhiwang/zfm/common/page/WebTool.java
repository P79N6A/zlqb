package com.zhiwang.zfm.common.page;

/**
 * Web工具
 * 
 * @author gaoyufeng
 * 
 */
public class WebTool {
	
	/**
	 * 获取页面索引对象
	 * @param viewpagecount
	 * @param currentPage
	 * @param totalpage
	 * @return
	 */
	public static PageIndex getPageIndex(long viewpagecount, int currentPage, long totalpage) {
		long startpage = currentPage - (viewpagecount % 2 == 0 ? viewpagecount / 2 - 1 : viewpagecount / 2);
		long endpage = currentPage + viewpagecount / 2;
		if (startpage < 1) {
			startpage = 1;
			if (totalpage >= viewpagecount)
				endpage = viewpagecount;
			else
				endpage = totalpage;
		}
		if (endpage > totalpage) {
			endpage = totalpage;
			if ((endpage - viewpagecount) > 0)
				startpage = endpage - viewpagecount + 1;
			else
				startpage = 1;
		}
		return new PageIndex(startpage, endpage);
	}
}
