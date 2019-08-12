package com.nyd.zeus.service.excel.hanlder;

import java.util.List;

/**
 * 行级别数据读取处理回调
 * Cong Yuxiang
 * 2017/11/21
 */
public interface ReadHandler {

	/**
	 * 处理当前行数据 从0开始
	 */
	void handler(int sheetIndex, int rowIndex, List<String> row);
}
