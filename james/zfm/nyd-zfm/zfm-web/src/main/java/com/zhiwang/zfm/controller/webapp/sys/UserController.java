package com.zhiwang.zfm.controller.webapp.sys;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.order.RoleAndUserVO;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;
import com.zhiwang.zfm.entity.sys.User;
//import com.zhiwang.zfm.service.api.sys.CompanyUserService;
import com.zhiwang.zfm.service.api.sys.UserService;

@RestController
@RequestMapping("/api/sys/user")
@Api(description = "后台用户管理")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService<User> userService;

	

	/*@Autowired
	CompanyUserService<CompanyUser> companyUserService;*/
	
	@SuppressWarnings("unchecked")
	@PostMapping("/login")
	@ApiOperation(value = "用户登录")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<UserVO> login(@ModelAttribute UserVO loginUser,HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<UserVO> result = null;
		try {
	        Subject subject = SecurityUtils.getSubject();
	        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
	        		loginUser.getLoginName(),
	        		loginUser.getPassword());
	        subject.login(usernamePasswordToken);
			result=(CommonResponse<UserVO>) CommonResponse.success("登录成功");
			result.setData((UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION));
			return result;
		}catch (IncorrectCredentialsException e) {
			return (CommonResponse<UserVO>) CommonResponse.error(e.getMessage(),StatusConstants.ERROR_CODE);
		}catch (UnknownAccountException e) {
			return (CommonResponse<UserVO>) CommonResponse.error(e.getMessage(),StatusConstants.ERROR_CODE);
		}catch (LockedAccountException e) {
			return (CommonResponse<UserVO>) CommonResponse.error(e.getMessage(),StatusConstants.ERROR_CODE);
		}catch (AuthenticationException e) {
			return (CommonResponse<UserVO>) CommonResponse.error(e.getMessage(),StatusConstants.ERROR_CODE);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e);
			logger.error(e.getMessage(), e);
			logger.error("用户登录异常,请求参数:{}");
			return (CommonResponse<UserVO>) CommonResponse.error(e.getMessage(),StatusConstants.ERROR_CODE);
		}
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/getUserInfo")
	@ApiOperation(value = "获取客户信息")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<UserVO> getUserInfo(HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<UserVO> result = new CommonResponse<>();

		try {
			UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
			if(Objects.isNull(userVO)) {
				return (CommonResponse<UserVO>) CommonResponse.error("请先登录");
			}
			result = (CommonResponse<UserVO>) CommonResponse.success("获取成功");
			userVO = userService.getUserByLoginName(userVO.getLoginName());
			ShiroUtils.setSessionAttribute(ShiroUtils.EMPLOYEE_SESSION,userVO);
			result.setData(userVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("用户登录异常,请求参数:{}");
			result = (CommonResponse<UserVO>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getUserModuleList")
	@ApiOperation(value = "获取用户菜单列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<List<ModuleVO>> getUserModuleList()throws IOException {
		CommonResponse<List<ModuleVO>> result=new CommonResponse<>();

		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		try {
			List<ModuleVO> moduleList=userService.getUserModuleList(userVO);
			result.setSuccess(true);;
			result.setData(moduleList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("获取用户资源异常,请求参数:{}");
			result = (CommonResponse<List<ModuleVO>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	@PostMapping("/userAdd")
	@ApiOperation(value = "用户新增")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> userAdd(@ModelAttribute UserVO user,HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<?> result = null;
		try {
			result=userService.saveUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("新增用户异常,请求参数:{}");
			result = CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/userlist")
	@ApiOperation(value = "用户列表查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<UserVO>> userList(@ModelAttribute UserVO queryUser,HttpServletRequest request) throws IOException {
		// 设置返回类型
		PagedResponse<List<UserVO>> result = null;
		try {
			if(null != queryUser){
				if(StringUtils.isNotBlank(queryUser.getLoginName())){
					queryUser.setLoginName(queryUser.getLoginName().trim());
				}
			}
			result = userService.getUsers(queryUser);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("查询用户异常,请求参数:{}",queryUser);
			result=(PagedResponse<List<UserVO>>) PagedResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@PostMapping("/logout")
	@ApiOperation(value = "用户注销")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> logout(HttpServletRequest request) throws IOException {
		ShiroUtils.removeSession(ShiroUtils.EMPLOYEE_SESSION);
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return CommonResponse.success("注销成功");
	}

	@PostMapping("/updatePassWord")
	@ApiOperation(value = "修改密码")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> updatePassWord(@ModelAttribute UserVO loginUser,HttpServletRequest request) {
		CommonResponse<?> result=new CommonResponse<>();
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		//验证是否的登录
		if(Objects.isNull(userVO)) {
			return CommonResponse.error("请先登录");
		}
		//验证账号是否正确
		if(!Objects.equals(loginUser.getLoginName(),userVO.getLoginName())) {
			return CommonResponse.error("账号不存在");
		}
		String password=loginUser.getPassword();
		if(Objects.isNull(password) || password.length()<6 || password.length()>16) {
			return CommonResponse.error("登录密码不可为空,且为6~16个字符");
		}
		//验证密码是否正确
		/*if(!Objects.equals(loginUser.getOldPassword(),userVO.getPassword())) {
			return CommonResponse.error("原始密码错误");
		}*/
		try {
			UserVO vo=new UserVO();
			vo.setId(userVO.getId());
			vo.setPassword(loginUser.getPassword());
			result=userService.updateUser(vo);
			userVO.setPassword(vo.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("修改密码异常,请求参数:{}",loginUser);
			result=CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	@PostMapping("/resetPassWord")
	@ApiOperation(value = "重置密码")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> resetPassWord(@RequestParam String userId,HttpServletRequest request) {
		CommonResponse<?> result=new CommonResponse<>();
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		//验证是否的登录
		if(Objects.isNull(userVO)) {
			return CommonResponse.error("请先登录");
		}
		
		try {
			result=userService.resetPassWord(userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("重置密码异常,请求参数:{}",userId);
			result=CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}


	@PostMapping("/updateUser")
	@ApiOperation(value = "修改用户信息")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> updateUser(@ModelAttribute UserVO userVO,HttpServletRequest request) {
		CommonResponse<?> result=new CommonResponse<>();
		try {
			result=userService.updateUser(userVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("修改用户异常,请求参数:{}",userVO);
			result=CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	@PostMapping("/removeUser")
	@ApiOperation(value = "删除用户")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> removeUser(@RequestParam String userId,HttpServletRequest request) {
		CommonResponse<?> result=null;
		try {
			result=userService.removeUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("删除用户异常,请求参数:{}",userId);
			result=CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	/**
	 * 功能说明： 查询推广专员
	 * 修改人：hudongbo
	 * 修改内容：
	 * 修改注意点：
	 */
	/*@SuppressWarnings("unchecked")
	@PostMapping("/querySpreadUser")
	@ApiOperation(value = "查询推广专员")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<List<JSONObject>> querySpreadUser(@RequestParam String channelId)  {
		CommonResponse<List<JSONObject>> result = new CommonResponse<List<JSONObject>>();
		try{
			RoleAndUserVO aalVO = new RoleAndUserVO();
			//查询channelId关联了哪些推广员
			List<String> userIdList=channelUserService.getUserIdListByChannelId(channelId);
			if(CollectionUtils.isNotEmpty(userIdList)) {
				//这些推广员将不再返回至可选择列表
				aalVO.setIgnoreUserIdList(userIdList);
			}
			aalVO.setRoleName("贷超");
			List<JSONObject> list =userService.getRoleAndLogin(aalVO);
			result.setSuccess(true);
			result.setData(list);
		}catch (Exception e) {
			logger.error("查询推广专员出现异常 ：" + e);
			logger.error("查询推广专员出现异常 ：请求参数{}");
			result=(CommonResponse<List<JSONObject>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}*/

	/**
	 * 功能说明： 根据角色查询用户:主要用户查询催收公司
	 * 修改人：hudongbo
	 * 修改内容：
	 * 修改注意点：
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/queryUserByRole")
	@ApiOperation(value = "根据角色查询用户:主要用户查询催收公司")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<List<JSONObject>> queryCompanyUser(@ModelAttribute RoleAndUserVO aalVO)  {
		CommonResponse<List<JSONObject>> result = new CommonResponse<List<JSONObject>>();
		try{
			List<JSONObject> list =userService.getRoleAndLogin(aalVO);
			result.setSuccess(true);
			result.setData(list);
		}catch (Exception e) {
			logger.error("根据角色查询用户:主要用户查询催收公司 ：" + e);
			logger.error("根据角色查询用户:主要用户查询催收公司 ：请求参数{}");
			result=(CommonResponse<List<JSONObject>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
}
