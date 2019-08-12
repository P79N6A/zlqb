package com.zhiwang.zfm.service.api.sys;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.order.RoleAndUserVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.CreditUserVo;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;

public interface UserService<T> {

	/**
	 * 获得角色-权限的相关信息
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	List<JSONObject> getRoleAndLogin(RoleAndUserVO aalVO);
	
	/**
	 * 用户新增
	 * 
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<?> saveUser(UserVO userVO) throws Exception;

	/**
	 * 通过登录名查询用户
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public UserVO getUserByLoginName(String loginName) throws Exception;

	/**
	 * 根据用户获取菜单
	 * 
	 * @param userVO
	 * @return
	 * @throws Exception
	 */
	public List<ModuleVO> getUserModuleList(UserVO userVO) throws Exception;

	/**
	 * 用户列表查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public PagedResponse<List<UserVO>> getUsers(UserVO user) throws Exception;

	/**
	 * 修改用户信息
	 * 
	 * @param userVO
	 * @throws Exception
	 */
	public CommonResponse<?> updateUser(UserVO userVO) throws Exception;

	/**
	 * 密码重置
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<?> resetPassWord(String userId) throws Exception; 
	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<?> removeUser(String userId) throws Exception;
	
	/**
	 * 获得通过公司公司id获取对应的催收专员
	 * 功能说明： 
	 * 修改人：yuanhao
	 * 修改内容：
	 * 修改注意点：
	 */
	public List<JSONObject> getUrgeEmpListByCompId(String compId);
	
	public List<JSONObject> getUserByUserRole();
	//查询启用中的信审专员
	public List<CreditUserVo> getCreditUser();
}
