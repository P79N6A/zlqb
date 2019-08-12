package com.zhiwang.zfm.dao.sys;

import java.io.Serializable;
import java.util.List;

import com.zhiwang.zfm.common.page.BaseMapper;
/**
 * Module Mapper
 *
 */
public interface ModuleMapper<T> extends BaseMapper<T> {
	

	/**
	 * 依据id集合查询记录
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	List<T> selectModleListByIdList(Serializable []ids) throws Exception;
	/**
	 * 依据id集合查询pid
	 * @param idList
	 * @return
	 * @throws Exception
	 */
	List<String> selectPidListByIdList(Serializable []ids) throws Exception;
}
