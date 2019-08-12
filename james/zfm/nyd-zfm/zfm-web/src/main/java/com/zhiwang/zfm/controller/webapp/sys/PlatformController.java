package com.zhiwang.zfm.controller.webapp.sys;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhiwang.zfm.common.request.sys.SysChannelReqVO;
import com.zhiwang.zfm.common.request.sys.SysPlatformReqVO;
import com.zhiwang.zfm.common.response.PagedResponse;
import com.zhiwang.zfm.common.response.StatusConstants;
import com.zhiwang.zfm.common.response.bean.sys.SysChannelVO;
import com.zhiwang.zfm.common.response.bean.sys.SysPlatformVO;
import com.zhiwang.zfm.service.api.sys.SysChannelService;
import com.zhiwang.zfm.service.api.sys.SysPlatformService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/sys")
@Api(description = "平台管理")
public class PlatformController {

	private static final Logger logger = LoggerFactory.getLogger(PlatformController.class);

	@Autowired
	private SysPlatformService<?> sysPlatformService;

	@Autowired
	private SysChannelService<?> sysChannelService;

	@PostMapping("/platform/list")
	@ApiOperation(value = "平台列表查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<SysPlatformVO>> platformList(@ModelAttribute SysPlatformReqVO vo) throws Exception {
		logger.info("/platform/list req:{}", JSONObject.toJSONString(vo));
		PagedResponse<List<SysPlatformVO>> page = new PagedResponse<>();
		List<SysPlatformVO> list = sysPlatformService.pageData(vo);
		page.setTotal(sysPlatformService.pageTotalRecord(vo));
		page.setData(list);
		page.setCode(StatusConstants.SUCCESS_CODE);
		page.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
		page.setSuccess(true);
		return page;
	}

	@PostMapping("/channel/list")
	@ApiOperation(value = "平台渠道列表查询")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<SysChannelVO>> channelList(@ModelAttribute SysChannelReqVO vo) throws Exception {
		logger.info("/channel/list req:{}", JSONObject.toJSONString(vo));
		PagedResponse<List<SysChannelVO>> page = new PagedResponse<>();
		List<SysChannelVO> list = sysChannelService.pageData(vo);
		page.setTotal(sysChannelService.pageTotalRecord(vo));
		page.setData(list);
		page.setCode(StatusConstants.SUCCESS_CODE);
		page.setMsg(StatusConstants.SYSTEM_OPERATION_SUCCESS_MSG);
		page.setSuccess(true);
		return page;
	}

}
