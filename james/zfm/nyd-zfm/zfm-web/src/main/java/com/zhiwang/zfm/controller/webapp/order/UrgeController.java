package com.zhiwang.zfm.controller.webapp.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.api.zzl.ZeusForWHServise;
import com.nyd.zeus.model.BillDistributionRecordRequest;
import com.nyd.zeus.model.BillDistributionRecordVo;
import com.nyd.zeus.model.UrgeOverdueReq;
import com.nyd.zeus.model.UrgeOverdueRespVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;
import com.zhiwang.zfm.service.api.sys.UserRoleService;

@RestController
@RequestMapping("/loan/urge")
@Api(description="贷后")
public class UrgeController {

    private static Logger LOGGER = LoggerFactory.getLogger(UrgeController.class);
	
	@Autowired
	private ZeusForWHServise zeusForWHServise;
	
	@Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@PostMapping("/overdueList")
	@ApiOperation(value = "贷后管理-全部订单")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<UrgeOverdueRespVo>> getOverdueList(@RequestBody UrgeOverdueReq req) {
		PagedResponse<List<UrgeOverdueRespVo>> pageResponse = new PagedResponse<List<UrgeOverdueRespVo>>();
		try {
			pageResponse  = zeusForWHServise.getOverdueList(req);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(),e);
			pageResponse.setSuccess(false);
		}
		return pageResponse;
	}
	
	@PostMapping("/doDistribution")
	@ApiOperation(value = "分配")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse doDistribution(@RequestBody BillDistributionRecordRequest vo) {
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		CommonResponse commonResponse = new CommonResponse();
		try {
			
			boolean isError = false;
			List<String> orderNos = vo.getOrderNos();
			if(orderNos!=null && orderNos.size()>0) {
				for(String orderNo:orderNos) {
					try {
						BillDistributionRecordVo disVo = new BillDistributionRecordVo();
						disVo.setOrderNo(orderNo);
						disVo.setCreateTime(new Date());
						disVo.setDistributionUserId(userVO.getId());
						disVo.setDistributionUserName(userVO.getLoginName());
						disVo.setReceiveUserId(vo.getReceiveUserId());
						disVo.setReceiveUserName(vo.getReceiveUserName());
						disVo.setUpdateTime(new Date());
						disVo.setPayStatus("2");
						zeusForOrderPayBackServise.doDistribution(disVo);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(),e);
						isError=true;
					}
				}
			}
			commonResponse.setSuccess(true);
			commonResponse.setMsg("分配成功!");
		
			return commonResponse;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(),e);
			commonResponse.setSuccess(false);
		}
		return commonResponse;
	}
	@PostMapping("/getCreditTrialUsersList")
	@ApiOperation(value = "获取贷后专员列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>> getCreditTrialUsersList() {
		com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>> pagedResponse = new com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>>();
		try {
			pagedResponse.setData(userRoleService.getCreditTrialUsersList("9"));
			pagedResponse.setSuccess(true);
			return pagedResponse;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(),e);
			pagedResponse.setSuccess(false);
		}
		return pagedResponse;
	}
	
	@PostMapping("/overdueAssignList")
	@ApiOperation(value = "贷后订单提醒")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<UrgeOverdueRespVo>> overdueAssignList(
			@RequestBody UrgeOverdueReq req) {
		PagedResponse<List<UrgeOverdueRespVo>> pageResponse = new PagedResponse<List<UrgeOverdueRespVo>>();
		try {
			// 贷后主管和超级管理员可查询所有的订单数据
			UserVO userVO = (UserVO) ShiroUtils
					.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
			if (!isAdmin(userVO)){
				req.setUserId(userVO.getId());
			}
			LOGGER.info("overdueAssignList req: {}",
					JSONObject.toJSONString(req));
			pageResponse = zeusForWHServise.getOverdueAssignList(req);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
			pageResponse.setSuccess(false);
		}
		return pageResponse;
	}
	
	// 判断是否是超管
	private boolean isAdmin(UserVO userVO) {
		List<String> roleList = userVO.getRoleIdList();
		if (roleList.contains("1") || roleList.contains("8"))
			return true;
		else
			return false;
	}
	
	
	
}
