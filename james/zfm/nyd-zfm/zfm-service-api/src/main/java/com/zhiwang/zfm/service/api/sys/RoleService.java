package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.RoleVO;

public interface RoleService<T> {

	/**
	 * 	角色列表查询
	 * @return
	 * @throws Exception
	 */
	public PagedResponse<List<RoleVO>> getRoles(RoleVO queryroleVO) throws Exception ;
	
	public CommonResponse<?> saveRole(RoleVO roleVO) throws Exception ;
	
	public CommonResponse<?> updateRole(RoleVO roleVO) throws Exception ;
	
	public CommonResponse<?> removeRole(String roleId) throws Exception ;

}
