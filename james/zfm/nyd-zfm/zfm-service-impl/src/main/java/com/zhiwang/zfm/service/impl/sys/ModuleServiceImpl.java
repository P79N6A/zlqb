package com.zhiwang.zfm.service.impl.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.ModuleTreeVO;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.dao.sys.ModuleMapper;
import com.zhiwang.zfm.dao.sys.RoleMapper;
import com.zhiwang.zfm.entity.sys.Module;
import com.zhiwang.zfm.entity.sys.Role;
import com.zhiwang.zfm.entity.sys.query.ModuleSearchForm;
import com.zhiwang.zfm.service.api.sys.ModuleService;

@Service
@Transactional
public class ModuleServiceImpl<T> implements ModuleService<T> {

	@Autowired
    private ModuleMapper<T> mapper;

	@Autowired
    private RoleMapper<Role> roleMapper;

	public ModuleMapper<T> getMapper() {
		return mapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> saveModule(ModuleVO moduleVO) throws Exception {
		Module module=BeanCommonUtils.copyProperties(moduleVO, Module.class);
		module.setStatus(1);
		module.setCreateTime(new Date());
		mapper.insert((T) module);
		String pid=module.getPid();
		if(!Objects.isNull(pid)) {
			List<Role> roleList=roleMapper.getRoleListByModuleId(pid);
			if(CollectionUtils.isNotEmpty(roleList)) {
				for (Role role : roleList) {
					Role roleUpdate=new Role();
					roleUpdate.setId(role.getId());
					String sysModuleIds=role.getSysModuleIds();
					if(Objects.isNull(sysModuleIds)) {
						roleUpdate.setSysModuleIds(module.getId().toString());
					}else {
						roleUpdate.setSysModuleIds(sysModuleIds+","+module.getId().toString());
					}
					roleMapper.updateBySelective(roleUpdate);
				}
			}
		}
		return CommonResponse.success("新增成功");
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> updateModule(ModuleVO moduleVO) throws Exception {
		if(Objects.isNull(mapper.selectById(moduleVO.getId()))) {
			return CommonResponse.error("菜单不存在");
		}
		Module module=BeanCommonUtils.copyProperties(moduleVO, Module.class);
		mapper.updateBySelective((T) module);
		return CommonResponse.success("修改成功");
	}

	@Override
	public PagedResponse<List<ModuleVO>> getModuleListPage(ModuleVO moduleVO) throws Exception {
		PagedResponse<List<ModuleVO>> result=new PagedResponse<>();
		ModuleSearchForm form=new ModuleSearchForm();
		BeanCommonUtils.copyProperties(moduleVO, form);
		form.setLikeName(form.getName());
		form.setName(null);
		form.setPage(moduleVO.getPageNo());
		form.setRows(moduleVO.getPageSize());
		LinkedHashMap<String, String> orderby=new LinkedHashMap<>();
		orderby.put("sort", "asc");
		form.setOrderby(orderby);
		@SuppressWarnings("unchecked")
		List<Module> list=(List<Module>) mapper.pageData(form);
		List<ModuleVO> data=BeanCommonUtils.copyListProperties(list, ModuleVO.class);
		long cz=mapper.count(form);
		result.setTotal(cz);
		result.setData(data);
		result.setSuccess(true);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<ModuleTreeVO> getModuleTreeByRoleId(String roleId) throws Exception {
		CommonResponse<ModuleTreeVO> result=new CommonResponse<>();
		ModuleTreeVO treeVO=new ModuleTreeVO();
		treeVO.setRoleId(roleId);
		result.setData(treeVO);
		Role role=roleMapper.selectById(roleId);
		String sysModuleIds=role.getSysModuleIds();
		if(!StringUtils.isEmpty(sysModuleIds)) {
			List<String> moduleIds=new ArrayList<>(Arrays.asList(sysModuleIds.split(",")));
			/*List<String> pidList=mapper.selectPidListByIdList(moduleIds.toArray(new String[0]));
			if(CollectionUtils.isNotEmpty(pidList)) {
				moduleIds.removeAll(pidList);
			}*/
			treeVO.setModuleIds(moduleIds);
		}
		ModuleSearchForm msfForm=new ModuleSearchForm();
		LinkedHashMap<String, String> orderby=new LinkedHashMap<>();
		orderby.put("sort", "asc");
		msfForm.setPid("0");
		msfForm.setOrderby(orderby);
		List<Module> moduleList=(List<Module>) mapper.pageData(msfForm);
		if(CollectionUtils.isNotEmpty(moduleList)) {
			List<ModuleVO> moduleVOList=BeanCommonUtils.copyListProperties(moduleList, ModuleVO.class);
			queryModuleChildren(moduleVOList);
			treeVO.setModuleList(moduleVOList);
		}
		result.setSuccess(true);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void queryModuleChildren(List<ModuleVO> moduleVOList) throws Exception {
		for (ModuleVO moduleVO : moduleVOList) {
			String moduleId=moduleVO.getId().toString();
			ModuleSearchForm msfFormChildren=new ModuleSearchForm();
			LinkedHashMap<String, String> orderby=new LinkedHashMap<>();
			msfFormChildren.setPid(moduleId);
			orderby.put("sort", "asc");
			msfFormChildren.setOrderby(orderby);
			List<Module> moduleListChildren=(List<Module>) mapper.pageData(msfFormChildren);
			if(CollectionUtils.isNotEmpty(moduleListChildren)) {
				List<ModuleVO> moduleVOListChildren=BeanCommonUtils.copyListProperties(moduleListChildren, ModuleVO.class);
				moduleVO.setChildrenModule(moduleVOListChildren);
				queryModuleChildren(moduleVOListChildren);
			}
		}
	}

	@Override
	public Set<String> getModuleListByRoleId(List<String> roleIdList) throws Exception {
		// TODO Auto-generated method stub
		Set<String> result=new HashSet<>();
		if(CollectionUtils.isNotEmpty(roleIdList)) {
			for (String roleId : roleIdList) {
				Role role=roleMapper.selectById(roleId);
				String moduleIds=role.getSysModuleIds();//查询每个角色所有资源菜单
				if(!StringUtils.isEmpty(moduleIds)) {
					@SuppressWarnings("unchecked")
					List<Module> moduleList=(List<Module>) mapper.selectModleListByIdList(moduleIds.split(","));
					if(CollectionUtils.isNotEmpty(moduleList)) {
						for (Module module : moduleList) {
							String url=module.getUrl();
							if(!StringUtils.isEmpty(url)) {
								result.add(url);
							}
						}
					}
				}
			}
		}
		return result;
	}
	
}
