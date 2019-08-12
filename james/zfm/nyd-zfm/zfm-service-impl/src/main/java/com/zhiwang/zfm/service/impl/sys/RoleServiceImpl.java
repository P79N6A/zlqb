package com.zhiwang.zfm.service.impl.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.RoleVO;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.dao.sys.RoleMapper;
import com.zhiwang.zfm.dao.sys.UserRoleMapper;
import com.zhiwang.zfm.entity.sys.Role;
import com.zhiwang.zfm.entity.sys.UserRole;
import com.zhiwang.zfm.entity.sys.query.RoleSearchForm;
import com.zhiwang.zfm.entity.sys.query.UserRoleSearchForm;
import com.zhiwang.zfm.service.api.sys.RoleService;

@Service
@Transactional
public class RoleServiceImpl<T> implements RoleService<T> {

	@Autowired
    private RoleMapper<T> mapper;

	@Autowired
    private UserRoleMapper<UserRole> userRoleMapper;

	public RoleMapper<T> getMapper() {
		return mapper;
	}

	@Override
	public PagedResponse<List<RoleVO>> getRoles(RoleVO queryroleVO) throws Exception {
		PagedResponse<List<RoleVO>> result = new PagedResponse<List<RoleVO>>();
		RoleSearchForm form = new RoleSearchForm();
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("update_time", "desc");
		BeanCommonUtils.copyProperties(queryroleVO, form);
		form.setLikeName(form.getName());
		form.setName(null);
		form.setStatus(1);
		form.setOrderby(orderby);
		form.setPage(queryroleVO.getPageNo());
		form.setRows(queryroleVO.getPageSize());
		long ct = mapper.count(form);
		@SuppressWarnings("unchecked")
		List<Role> roles=(List<Role>) mapper.pageData(form);
		List<RoleVO> roleVOs=new ArrayList<>();
		for (Role role : roles) {
			RoleVO roleVO=BeanCommonUtils.copyProperties(role, RoleVO.class);
			roleVOs.add(roleVO);
		}
		result.setData(roleVOs);
		result.setTotal(ct);
		result.setSuccess(true);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> saveRole(RoleVO roleVO) throws Exception {
		
		RoleSearchForm form=new RoleSearchForm();
		form.setName(roleVO.getName());
		form.setStatus(1);
		if(roleVO.getName()==null ||roleVO.getName().trim().equals("")) {
			return CommonResponse.error("角色名不能为空");
		}
		roleVO.setName(roleVO.getName().trim());
		if(mapper.count(form)>0) {
			return CommonResponse.error("角色名已存在，请重新输入");
		}
		Role role=BeanCommonUtils.copyProperties(roleVO, Role.class);
		role.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		role.setStatus(1);
		Date createTime=new Date();
		role.setCreateTime(createTime);
		role.setUpdateTime(createTime);
		mapper.insert((T) role);
		return CommonResponse.success("新增成功");
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> updateRole(RoleVO roleVO) throws Exception {
		Role role=(Role) mapper.selectById(roleVO.getId());
		if(Objects.isNull(role)) {
			return CommonResponse.error("该角色不存在");
		}
		String roleName=roleVO.getName();
		if(!Objects.isNull(roleName)) {
			if(!Objects.equals(role.getName(), roleName)) {
				RoleSearchForm form=new RoleSearchForm();
				form.setName(roleVO.getName());
				form.setStatus(1);
				if(mapper.count(form)>0) {
					return CommonResponse.error("角色名已存在，请重新输入");
				}
			}
		}
		Role roleUpdate=BeanCommonUtils.copyProperties(roleVO, Role.class);
		roleUpdate.setUpdateTime(new Date());
		mapper.updateBySelective((T) roleUpdate);
		return CommonResponse.success("修改成功");
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> removeRole(String roleId) throws Exception {
		Role role=(Role) mapper.selectById(roleId);
		if(Objects.isNull(role)) {
			return CommonResponse.error("删除失败,该角色不存在");
		}
		//判断角色是否有绑定用户
		UserRoleSearchForm form=new UserRoleSearchForm();
		form.setRoleId(roleId);
		if(userRoleMapper.pageTotalRecord(form)>0) {
			return CommonResponse.error("删除失败,该角色之下存在用户,请先将该角色下的用户解绑");
		}
		Role roleUpdate=new Role();
		roleUpdate.setId(roleId);
		roleUpdate.setStatus(0);
		mapper.updateBySelective((T) roleUpdate);
		return CommonResponse.success("删除成功");
	}
}
