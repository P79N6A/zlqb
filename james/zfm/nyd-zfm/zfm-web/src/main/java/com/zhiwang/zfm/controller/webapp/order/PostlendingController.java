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
import com.nyd.zeus.api.zzl.ZeusForZQServise;
import com.nyd.zeus.model.BillDistributionRecordRequest;
import com.nyd.zeus.model.BillDistributionRecordVo;
import com.nyd.zeus.model.BillRepayListVO;
import com.nyd.zeus.model.ReadyDistributionRequest;
import com.nyd.zeus.model.ReadyDistributionVo;
import com.nyd.zeus.model.ReminderDistributionRequest;
import com.nyd.zeus.model.ReminderDistributionVo;
import com.nyd.zeus.model.common.CommonResponse;
import com.nyd.zeus.model.common.PagedResponse;
import com.nyd.zeus.model.common.response.BillRepayVo;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;
import com.zhiwang.zfm.service.api.sys.UserRoleService;

@RestController
@RequestMapping("/loan/postlending")
@Api(description="贷中")
public class PostlendingController {

    private static Logger LOGGER = LoggerFactory.getLogger(PostlendingController.class);
	
	@Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
	
	
	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private ZeusForZQServise zeusForZQServise;
	
	@PostMapping("/getReadyDistributionList")
	@ApiOperation(value = "贷中分配列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<ReadyDistributionVo>> getReadyDistributionList(@RequestBody ReadyDistributionRequest vo) {
		PagedResponse<List<ReadyDistributionVo>> pageResponse = new PagedResponse<List<ReadyDistributionVo>>();
		try {
			
			pageResponse  = zeusForOrderPayBackServise.queryPostlendingDistributionList(vo);
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
						disVo.setPayStatus("1");
						zeusForOrderPayBackServise.doDistribution(disVo);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(),e);
						isError=true;
					}
				}
			}
//			commonResponse.setSuccess(true);
//			commonResponse.setMsg("分配成功!");
			if(isError && orderNos.size()<2) {
				commonResponse.setMsg("分配失败!");
				commonResponse.setSuccess(false);
			}else if (isError && orderNos.size()>1) {
				commonResponse.setMsg("部分分配失败!");
				commonResponse.setSuccess(true);
			}else {
				commonResponse.setMsg("分配成功!");
				commonResponse.setSuccess(true);
			}
			return commonResponse;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(),e);
			commonResponse.setSuccess(false);
		}
		return commonResponse;
	}
	@PostMapping("/getCreditTrialUsersList")
	@ApiOperation(value = "获取提醒专员列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>> getCreditTrialUsersList() {
		com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>> pagedResponse = new com.zhiwang.zfm.common.response.CommonResponse<List<UserVO>>();
		try {
			pagedResponse.setData(userRoleService.getCreditTrialUsersList("7"));
			pagedResponse.setSuccess(true);
			return pagedResponse;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(),e);
			pagedResponse.setSuccess(false);
		}
		return pagedResponse;
	}
	
	@PostMapping("/getReminderDistributionList")
	@ApiOperation(value = "贷中提醒列表")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<ReminderDistributionVo>> getReminderDistributionList(
			@RequestBody ReminderDistributionRequest vo) {
		PagedResponse<List<ReminderDistributionVo>> pageResponse = new PagedResponse<List<ReminderDistributionVo>>();
		try {
			UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
			// 提醒主管和超级管理员可查询所有的订单数据
			if (!isAdmin(userVO)){
				vo.setUserId(userVO.getId());
			}
			LOGGER.info("reminderDistributionList req: {}",
					JSONObject.toJSONString(vo));
			pageResponse = zeusForOrderPayBackServise.queryReminderDistributionList(vo);
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
		if (roleList.contains("1") || roleList.contains("6"))
			return true;
		else
			return false;
	}
	
	/*@PostMapping("/judgingWithdraw")
	@ApiOperation(value = "是否可以撤回")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> judgingWithdraw() {
		if (isAdmin())
			return CommonResponse.success("可以撤回");
		else
			return CommonResponse.error("不可以撤回");
	}
	
	@PostMapping("/withdraw")
	@ApiOperation(value = "撤回")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> withdraw(
			@RequestBody BillDistributionWithdrawRequest vo) {
		UserVO userVO = (UserVO) ShiroUtils
				.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		if (!isAdmin())
			return CommonResponse.error("不可以撤回");
		int count = zeusForOrderPayBackServise.doDistributionInvalid(
				vo.getOrderNoList(), userVO.getId(), userVO.getLoginName());
		return CommonResponse.success(String.valueOf(count));
	}*/
	
	
	@PostMapping("/getBillRepayList")
	@ApiOperation(value = "还款记录")
	@Produces(value = MediaType.APPLICATION_JSON)
	public PagedResponse<List<BillRepayVo>> getBillRepayList(@RequestBody BillRepayListVO vo) {
		LOGGER.info("getBillRepayList req: {}",JSONObject.toJSONString(vo));
		return zeusForZQServise.getBillRepayList(vo);
	}
}
