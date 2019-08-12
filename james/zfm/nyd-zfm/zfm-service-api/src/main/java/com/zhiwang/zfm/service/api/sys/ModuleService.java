package com.zhiwang.zfm.service.api.sys;

import java.util.List;
import java.util.Set;

import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.ModuleTreeVO;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;

public interface ModuleService<T> {
	
	public CommonResponse<?> saveModule(ModuleVO moduleVO) throws Exception;
	
	public CommonResponse<?> updateModule(ModuleVO moduleVO) throws Exception;
	
	public PagedResponse<List<ModuleVO>> getModuleListPage(ModuleVO moduleVO) throws Exception;
	
	public CommonResponse<ModuleTreeVO> getModuleTreeByRoleId(String roleId) throws Exception;
	
	public Set<String> getModuleListByRoleId(List<String> roleIdList) throws Exception;
}
