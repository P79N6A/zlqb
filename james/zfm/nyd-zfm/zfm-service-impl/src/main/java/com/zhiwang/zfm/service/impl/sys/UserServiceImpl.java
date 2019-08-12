package com.zhiwang.zfm.service.impl.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.PageConfig;
import com.zhiwang.zfm.common.request.order.RoleAndUserVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.CreditUserVo;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.common.util.bean.BeanCommonUtils;
import com.zhiwang.zfm.common.util.security.MD5;
import com.zhiwang.zfm.dao.sys.CompanyUserMapper;
import com.zhiwang.zfm.dao.sys.ModuleMapper;
import com.zhiwang.zfm.dao.sys.RoleMapper;
import com.zhiwang.zfm.dao.sys.UserMapper;
import com.zhiwang.zfm.dao.sys.UserRoleMapper;
import com.zhiwang.zfm.entity.sys.CompanyUser;
import com.zhiwang.zfm.entity.sys.Module;
import com.zhiwang.zfm.entity.sys.Role;
import com.zhiwang.zfm.entity.sys.User;
import com.zhiwang.zfm.entity.sys.UserRole;
import com.zhiwang.zfm.entity.sys.query.CompanyUserSearchForm;
import com.zhiwang.zfm.entity.sys.query.RoleAndUserForm;
import com.zhiwang.zfm.entity.sys.query.RoleSearchForm;
import com.zhiwang.zfm.entity.sys.query.UserRoleSearchForm;
import com.zhiwang.zfm.entity.sys.query.UserSearchForm;
import com.zhiwang.zfm.service.api.sys.UserService;

@Service
@Transactional
public class UserServiceImpl<T> implements UserService<T> {

	@Override
	public UserVO getUserByLoginName(String loginName) throws Exception {
		UserVO userVO=null;
		UserSearchForm form = new UserSearchForm();
		form.setLoginName(loginName);
		form.setDelStatus(1);//未删除
		List<T> users=mapper.pageData(form);
		if(Objects.nonNull(users) && users.size()>0) {
			userVO = getUserVO(null, new StringBuffer(), (User)users.get(0));
		}
		return userVO;
	}

	@Override
	public PagedResponse<List<UserVO>> getUsers(UserVO userVO) throws Exception{
		PagedResponse<List<UserVO>> result = new PagedResponse<List<UserVO>>();
		UserSearchForm form = new UserSearchForm();
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("create_time", "desc");
		BeanCommonUtils.copyProperties(userVO, form);
		form.setLikeLoginName(userVO.getLoginName());
		form.setLoginName(null);
		form.setOrderby(orderby);
		form.setDelStatus(1);
		form.setPage(userVO.getPageNo());
		form.setRows(userVO.getPageSize());
		long ct = mapper.count(form);
		@SuppressWarnings("unchecked")
		List<User> users=(List<User>) mapper.pageData(form);
		List<UserVO> userVOs = new ArrayList<>();
		StringBuffer roleNameStr=new StringBuffer();
		for (User user : users) {
			roleNameStr.setLength(0);
			UserVO vo = getUserVO(orderby, roleNameStr, user);
			
			CompanyUserSearchForm companyForm=new CompanyUserSearchForm();
			companyForm.setUserId(user.getId());
			companyForm.setStatus(1);
			companyForm.setPage(1);
			companyForm.setRows(PageConfig.DEFAULT.getMaxPageSize());
			List<CompanyUser> companyUserList=companyUserMapper.pageData(companyForm);
			if(CollectionUtils.isNotEmpty(companyUserList)) {
				List<String> companyIdList=new ArrayList<>();
				StringBuffer companyName=new StringBuffer();
				for (CompanyUser companyUser : companyUserList) {
					String companyId=companyUser.getCompanyId();
					companyIdList.add(companyId);
					User company=(User) mapper.selectById(companyId);
						companyName.append(company.getLoginName()).append(" ");
				}
				if(CollectionUtils.isNotEmpty(companyIdList)) {
					vo.setCompanyIdList(companyIdList.get(0));
				}
				vo.setCompanyName(companyName.toString());
			}
			vo.setPassword(null);
			userVOs.add(vo);
		}
		result.setData(userVOs);
		result.setTotal(ct);
		result.setSuccess(true);
		return result;
	}

	private UserVO getUserVO(LinkedHashMap<String, String> orderby, StringBuffer roleNameStr, User user)
			throws Exception {
		UserVO vo=BeanCommonUtils.copyProperties(user, UserVO.class);
		UserRoleSearchForm userRoleForm=new UserRoleSearchForm();
		List<String> roleIds=new ArrayList<>();
		if(Objects.nonNull(orderby)) {
			userRoleForm.setOrderby(orderby);
		}
		userRoleForm.setUserId(user.getId());
		List<UserRole> userRoles=userRoleMapper.pageData(userRoleForm);
		if(CollectionUtils.isNotEmpty(userRoles)) {
			for (UserRole userRole : userRoles) {
				Role role=roleMapper.selectById(userRole.getRoleId());
				if(!Objects.isNull(role)) {
					roleNameStr.append(role.getName()).append(" ");
					roleIds.add(userRole.getRoleId());
				}
			}
			vo.setRoleIdList(roleIds);
			vo.setRoleName(roleNameStr.toString());
		}
		vo.setStatusMsg(vo.getStatus()==1?"正常":"禁用");
		return vo;
	}

	@Autowired
    private UserMapper<T> mapper;
	@Autowired
    private RoleMapper<Role> roleMapper;
	@Autowired
	private UserRoleMapper<UserRole> userRoleMapper;
	@Autowired
	ModuleMapper<Module> moduleMapper;
	@Autowired
	CompanyUserMapper<CompanyUser> companyUserMapper;

	public UserMapper<T> getMapper() {
		return mapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> saveUser(UserVO userVO) throws Exception {
		String loginName=userVO.getLoginName();
		String password=userVO.getPassword();
		if(Objects.isNull(loginName) || loginName.trim().length()<1 || loginName.trim().length()>20) {
			return CommonResponse.error("登录名不可为空,且为1~20个字符");
		}
		// 去除前后空格保存
		loginName = loginName.trim();
		userVO.setLoginName(loginName);
		if(Objects.isNull(password) || password.length()<6 || password.length()>16) {
			return CommonResponse.error("登录密码不可为空,且为6~16个字符");
		}
		//判断登录名是否已经存在
		UserSearchForm form = new UserSearchForm();
		form.setLoginName(loginName);
		form.setDelStatus(1);
		long ct = mapper.count(form);
		if(ct>0) {
			return CommonResponse.error("用户名已存在，请重新输入");
		}
		//判断角色id是否为空,必填项
		List<String> roleIdList=userVO.getRoleIdList();
		if(CollectionUtils.isEmpty(roleIdList)) {
			return CommonResponse.error("请选定角色");
		}
		List<String> roleListTemp = new ArrayList<>();
        for(int i=0;i<roleIdList.size();i++){  
            if(!roleListTemp.contains(roleIdList.get(i))){  
                roleListTemp.add(roleIdList.get(i));  
            }  
        }  
        roleIdList=roleListTemp;
		//移除无效角色id
		RoleSearchForm roleForm=new RoleSearchForm();
		for (int i = roleIdList.size()-1; i >= 0; i--) {
			roleForm.setId(roleIdList.get(i));
			if(roleMapper.count(roleForm)==0){
				roleIdList.remove(i);
			}
		}
		if(CollectionUtils.isEmpty(roleIdList)) {
			return CommonResponse.error("请选定有效角色");
		}
		Date createTime=new Date();
		//将VO转换成数据库实体
		User user = BeanCommonUtils.copyProperties(userVO, User.class);
		user.setPassword(MD5.GetMD5Code(password));
		user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		user.setDelStatus(1);
		user.setCreateTime(createTime);
		mapper.insert((T) user);
		String userId=user.getId();
		//将用户与角色关联
		for (String roleId : roleIdList) {
			UserRole ur=new UserRole();
			ur.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			ur.setCreateTime(createTime);
			userRoleMapper.insert(ur);
		}
		//判断是否关联公司,无效的公司id移除,有效则进行关联
		String companyIdStr=userVO.getCompanyIdList();
		if(!StringUtils.isEmpty(companyIdStr)) {
			List<String> companyIdList=new ArrayList<>();
			companyIdList.add(companyIdStr);
			if(CollectionUtils.isNotEmpty(companyIdList)) {
				form=new UserSearchForm();
				for (int i = companyIdList.size()-1; i >=0 ; i--) {
					form.setId(companyIdList.get(i));
					if(mapper.count(form)==0){
						companyIdList.remove(i);
					}
				}
				for (String companyId : companyIdList) {
					CompanyUser companyUser=new CompanyUser();
					companyUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					companyUser.setUserId(userId);
					companyUser.setCompanyId(companyId);
					companyUser.setStatus(1);
					companyUser.setCreateTime(createTime);
					companyUserMapper.insert(companyUser);
				}
			}
		}
		
		return CommonResponse.success("新增成功");
	}

	@SuppressWarnings("unchecked")
	public CommonResponse<?> updateUser(UserVO userVO) throws Exception {
		//判断客户是否存在
		String userId=userVO.getId();
		User user=(User) mapper.selectById(userId);
		if(Objects.isNull(user)) {
			return CommonResponse.error("该客户不存在");
		}
		//如果要修改登录名,判断新登录名是否已经存在
		String loginName=userVO.getLoginName();
		if(!Objects.isNull(loginName)) {
			if(Objects.isNull(loginName) || loginName.trim().length()<1 || loginName.trim().length()>20) {
				return CommonResponse.error("登录名不可为空,且为1~20个字符");
			}
			// 去除前后空格保存
			loginName = loginName.trim();
			userVO.setLoginName(loginName);
			if(!Objects.equals(user.getLoginName(), loginName)) {
				UserSearchForm form = new UserSearchForm();
				form.setLoginName(userVO.getLoginName());
				form.setDelStatus(1);
				long ct = mapper.count(form);
				if(ct>0) {
					return CommonResponse.error("用户名已存在，请重新输入");
				}
			}
		}
		//判断角色id是否有效,无效的移除,有效的关联
		Date createTime=new Date();
		List<String> roleIdList=userVO.getRoleIdList();
		if(CollectionUtils.isNotEmpty(roleIdList)){
			List<String> roleListTemp = new ArrayList<>();
	        for(int i=0;i<roleIdList.size();i++){  
	            if(!roleListTemp.contains(roleIdList.get(i))){  
	                roleListTemp.add(roleIdList.get(i));  
	            }  
	        }  
	        roleIdList=roleListTemp;
			RoleSearchForm roleForm=new RoleSearchForm();
			for (int i = roleIdList.size()-1; i >= 0; i--) {
				roleForm.setId(roleIdList.get(i));
				if(roleMapper.count(roleForm)==0){
					roleIdList.remove(i);
				}
			}
			if(CollectionUtils.isEmpty(roleIdList)) {
				return CommonResponse.error("请选定有效角色");
			}
			UserRoleSearchForm ursf=new UserRoleSearchForm();
			ursf.setUserId(userVO.getId());
			List<UserRole> urList=userRoleMapper.pageData(ursf);
			if(CollectionUtils.isNotEmpty(urList)) {
				String[] urIdArr=new String[urList.size()];
				for (int i = 0; i < urList.size(); i++) {
					urIdArr[i]=urList.get(i).getId();
				}
				userRoleMapper.batchDelete(urIdArr);
			}
			for (String roleId : roleIdList) {
				UserRole ur=new UserRole();
				ur.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				ur.setUserId(userId);
				ur.setRoleId(roleId);
				ur.setCreateTime(createTime);
				userRoleMapper.insert(ur);
			}
		}
		//判断公司是否有效,无效移除,有效关联
		String companyIdStr=userVO.getCompanyIdList();
		if(!Objects.isNull(companyIdStr)) {

			CompanyUserSearchForm companyUserSearchForm= new CompanyUserSearchForm();
			companyUserSearchForm.setUserId(userId);
			List<CompanyUser> companyUserList=null;//companyUserMapper.pageData(companyUserSearchForm);
			if(CollectionUtils.isNotEmpty(companyUserList)) {
				String[] companyUserIdArr=new String[companyUserList.size()];
				for (int i = 0; i < companyUserList.size(); i++) {
					companyUserIdArr[i]=companyUserList.get(i).getId();
				}
				//companyUserMapper.batchDelete(companyUserIdArr);
			}
			if(!StringUtils.isEmpty(companyIdStr)) {
				List<String> companyIdList=new ArrayList<>();
				companyIdList.add(companyIdStr);
				if(!Objects.isNull(companyIdList)) {
					UserSearchForm form = new UserSearchForm();
					form.setDelStatus(1);
					for (int i = companyIdList.size()-1; i >=0 ; i--) {
						form.setId(companyIdList.get(i));
						if(mapper.count(form)==0){
							companyIdList.remove(i);
						}
					}
					if(CollectionUtils.isEmpty(companyIdList)) {
						return CommonResponse.error("请选定有效公司");
					}
					for (String companyId : companyIdList) {
						CompanyUser companyUser=new CompanyUser();
						companyUser.setId(UUID.randomUUID().toString().replaceAll("-", ""));
						companyUser.setUserId(userId);
						companyUser.setCompanyId(companyId);
						companyUser.setStatus(1);
						companyUser.setCreateTime(createTime);
						//companyUserMapper.insert(companyUser);
					}
				}
			}
		}
		User userUpdate=BeanCommonUtils.copyProperties(userVO, User.class);
		String password=userUpdate.getPassword();
		if(!Objects.isNull(password)) {
			userUpdate.setPassword(MD5.GetMD5Code(password));
		}
		mapper.updateBySelective((T) userUpdate);
		return CommonResponse.success("修改成功");
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> resetPassWord(String userId) throws Exception {
		//判断客户是否存在
		User user=(User) mapper.selectById(userId);
		if(Objects.isNull(user)) {
			return CommonResponse.error("该客户不存在");
		}
		User userUpdate=new User();
		userUpdate.setId(userId);
		userUpdate.setPassword(MD5.GetMD5Code(User.defaultPW));
		mapper.updateBySelective((T) userUpdate);
		return CommonResponse.success("重置密码成功");
	}
	
	public static void main(String[] args) {

	}

	@Override
	public List<ModuleVO> getUserModuleList(UserVO userVO) throws Exception {
		List<ModuleVO> moduleVOList=new ArrayList<>();
		List<String> roleList=userVO.getRoleIdList();//查询用户所有角色
		Set<String> idSet=new HashSet<>();
		
		for (String roleId : roleList) {
			Role role=roleMapper.selectById(roleId);
			String moduleIds=role.getSysModuleIds();//查询每个角色所有资源菜单 并去重
			if(!StringUtils.isEmpty(moduleIds)) {
				String[]  idArr=moduleIds.split(",");
				if(!Objects.isNull(idArr) && idArr.length>0) {
					idSet.addAll(new ArrayList<>(Arrays.asList(idArr)));
				}
			}
		}
		if(CollectionUtils.isNotEmpty(idSet)) {
			String[] moduleIdArr=idSet.toArray(new String[] {});
			while(true) {
				if(Objects.nonNull(moduleIdArr) && moduleIdArr.length>0) {
					List<String>  modulePids=moduleMapper.selectPidListByIdList(moduleIdArr);
					if(CollectionUtils.isNotEmpty(modulePids)) {
						idSet.addAll(modulePids);
						moduleIdArr=modulePids.toArray(new String[] {});
						continue;
					}
				}
				break;
			}
			List<Module> moduleList=moduleMapper.selectModleListByIdList(idSet.toArray(new String[] {}));//获取用户所有菜单权限
			for (int i = moduleList.size()-1; i >=0; i--) {
				Module module=moduleList.get(i);
				if(Objects.equals(module.getPid(), "0")) {
					moduleList.remove(i);
					ModuleVO moduleVO=BeanCommonUtils.copyProperties(module, ModuleVO.class);
					moduleVOList.add(moduleVO);//获取一级菜单
				}
			}
			if(CollectionUtils.isNotEmpty(moduleVOList)) {
				setModuleChildrenList(moduleVOList, moduleList);
			}

			/*for (ModuleVO moduleVO : moduleVOList) {//通过一级目录查询相关二级目录
				List<ModuleVO> moduleVOChildrenList=new ArrayList<>();
				for (int i = moduleList.size()-1; i >=0; i--) {
					Module module=moduleList.get(i);
					if(Objects.equals(module.getPid(), moduleVO.getId().toString())) {
						moduleList.remove(i);
						ModuleVO moduleVOChildren=BeanCommonUtils.copyProperties(module, ModuleVO.class);
						moduleVOChildrenList.add(moduleVOChildren);
					}
				}
				if(CollectionUtils.isNotEmpty(moduleVOChildrenList)) {
					moduleVO.setChildrenModule(moduleVOChildrenList);
				}
			}*/
		}
		return moduleVOList;
	}

	private void setModuleChildrenList(List<ModuleVO> moduleVOList, List<Module> moduleList) {
		if( CollectionUtils.isNotEmpty(moduleList) && CollectionUtils.isNotEmpty(moduleVOList) ) {
			for (ModuleVO moduleVOTemp : moduleVOList) {
				List<ModuleVO> moduleVOTempChildrenList=new ArrayList<>();
				for (int j = moduleList.size()-1; j >=0; j--) {
					Module ModuleTemp = moduleList.get(j);
					if(Objects.equals(moduleVOTemp.getId().toString(), ModuleTemp.getPid())) {
						moduleList.remove(j);
						ModuleVO moduleVO=BeanCommonUtils.copyProperties(ModuleTemp, ModuleVO.class);
						moduleVOTempChildrenList.add(moduleVO);
					}
				}
				if(CollectionUtils.isNotEmpty(moduleVOTempChildrenList)) {
					moduleVOTemp.setChildrenModule(moduleVOTempChildrenList);
					setModuleChildrenList(moduleVOTempChildrenList, moduleList);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonResponse<?> removeUser(String userId) throws Exception {
		//判断用户是否存在
		if(Objects.isNull(mapper.selectById(userId))){
			return CommonResponse.error("删除失败,用户不存在");
		}	
		User user=new User();
		user.setId(userId);
		user.setDelStatus(0);
		mapper.updateBySelective((T) user);
		return CommonResponse.success("删除成功");
	}
	
	/**
	 * 获得角色-权限的相关信息
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public List<JSONObject> getRoleAndLogin(RoleAndUserVO aalVO){
		RoleAndUserForm rau  = BeanCommonUtils.copyProperties(aalVO, RoleAndUserForm.class);
		return mapper.getRoleAndLogin(rau);
	}
	/**
	 * 获得通过公司公司id获取对应的催收专员
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public List<JSONObject> getUrgeEmpListByCompId(String compId){
		return mapper.getUrgeEmpListByCompId(compId);
	}

	@Override
	public List<JSONObject> getUserByUserRole() {
		
		return mapper.getUserList();
	}

	/**
	 * 查询启用中的信审专员
	 */
	@Override
	public List<CreditUserVo> getCreditUser() {
		return mapper.getCreditUser();
	}
}
