package com.zhiwang.zfm.controller.webapp.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.response.bean.sys.RoleVO;
import com.zhiwang.zfm.entity.sys.Role;
import com.zhiwang.zfm.service.api.sys.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/sys/role")
@Api(description = "后台角色管理")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService<Role> roleService;

	@PostMapping("/addRole")
	@ApiOperation(value = "新增角色")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> addRole(@ModelAttribute RoleVO roleVO,HttpServletRequest request) {
		CommonResponse<?> result=null;
		try {
			result=roleService.saveRole(roleVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("新增角色异常,请求参数:{}",roleVO);
			result =CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@PostMapping("/updateRole")
	@ApiOperation(value = "角色修改")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> updateRole(@ModelAttribute RoleVO roleVO,HttpServletRequest request) {
		CommonResponse<?> result=null;
		try {
			result=roleService.updateRole(roleVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("查询角色异常,请求参数:{}",roleVO);
			result =CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/rolelist")
	@ApiOperation(value = "角色列表查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<RoleVO>> roleList(@ModelAttribute RoleVO roleVO) {
		// 设置返回类型
		PagedResponse<List<RoleVO>> result = null;
		try {
			result = roleService.getRoles(roleVO);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("查询角色异常,请求参数:{}");
			result =(PagedResponse<List<RoleVO>>) PagedResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@PostMapping("/removeRole")
	@ApiOperation(value = "删除角色")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> removeRole(@RequestParam String roleId,HttpServletRequest request) {
		CommonResponse<?> result=null;
		try {
			result=roleService.removeRole(roleId);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("删除角色异常,请求参数:{}",roleId);
			result=new CommonResponse<>();
			result.setSuccess(false);
		}
		return result;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
