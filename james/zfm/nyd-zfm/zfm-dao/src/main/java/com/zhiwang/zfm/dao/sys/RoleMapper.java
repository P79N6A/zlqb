package com.zhiwang.zfm.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhiwang.zfm.common.page.BaseMapper;
/**
 * Role Mapper
 *
 */
public interface RoleMapper<T> extends BaseMapper<T> {
	
	/**
	 * 根据菜单id查询角色
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public List<T> getRoleListByModuleId(@Param("moduleId") String moduleId) throws Exception;
}
