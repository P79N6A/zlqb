package com.zhiwang.zfm.controller.webapp.sys;

import java.io.IOException;
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

import com.zhiwang.zfm.common.request.PageConfig;
import com.zhiwang.zfm.common.response.CommonResponse;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.response.bean.sys.ModuleTreeVO;
import com.zhiwang.zfm.common.response.bean.sys.ModuleVO;
import com.zhiwang.zfm.entity.sys.Module;
import com.zhiwang.zfm.service.api.sys.ModuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/sys/module")
@Api(description = "后台资源管理")
public class ModuleController {

	private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);

	@Autowired
	private ModuleService<Module> moduleService;
	
	@PostMapping("/addModule")
	@ApiOperation(value = "新增菜单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> addModule(@ModelAttribute ModuleVO moduleVO,HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<?> result = null;
		try {
			result =moduleService.saveModule(moduleVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("新增资源异常,请求参数:{}",moduleVO);
			result = CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@PostMapping("/updateModule")
	@ApiOperation(value = "修改菜单资源")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> updateModule(@ModelAttribute ModuleVO moduleVO,HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<?> result = null;
		try {
			result=moduleService.updateModule(moduleVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("修改资源异常,请求参数:{}",moduleVO);
			result = CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	

	@SuppressWarnings("unchecked")
	@PostMapping("/getParentModule")
	@ApiOperation(value = "获取父级菜单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<List<ModuleVO>> getParentModule(HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<List<ModuleVO>> result = new CommonResponse<>();
		try {
			ModuleVO moduleVO=new ModuleVO();
			moduleVO.setPid("0");
			moduleVO.setStatus(1);
			moduleVO.setHasNode(1);
			moduleVO.setPageNo(1);
			moduleVO.setPageSize(PageConfig.DEFAULT.getMaxPageSize());
			PagedResponse<List<ModuleVO>> pageModuleList=moduleService.getModuleListPage(moduleVO);
			List<ModuleVO> voList=pageModuleList.getData();
			ModuleVO parentTemp=new ModuleVO();
			parentTemp.setId(0);
			parentTemp.setName("根目录");
			voList.add(0, parentTemp);
			result.setData(voList);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("获取父级菜单异常");
			result = (CommonResponse<List<ModuleVO>>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/moduleList")
	@ApiOperation(value = "用户列表查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<ModuleVO>> moduleList(@ModelAttribute ModuleVO moduleVO,HttpServletRequest request) throws IOException {
		// 设置返回类型
		PagedResponse<List<ModuleVO>> result = null;
		try {
			result = moduleService.getModuleListPage(moduleVO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("查询资源异常,请求参数:{}",moduleVO);
			result = (PagedResponse<List<ModuleVO>>) PagedResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/getModuleListByRoleId")
	@ApiOperation(value = "菜单树")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<ModuleTreeVO> getModuleListByRoleId(@RequestParam String roleId,HttpServletRequest request) throws IOException {
		// 设置返回类型
		CommonResponse<ModuleTreeVO> result = null;
		try {
			result=moduleService.getModuleTreeByRoleId(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			logger.error("修改资源异常,请求参数:{}",roleId);
			result =(CommonResponse<ModuleTreeVO>) CommonResponse.error(StatusConstants.SYS_ERROR_MSG);
		}
		return result;
	}
	
	
	public CommonResponse<List<ModuleVO>> getModule() {
		
		return null;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
