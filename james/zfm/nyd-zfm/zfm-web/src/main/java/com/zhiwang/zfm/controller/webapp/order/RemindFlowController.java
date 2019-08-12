package com.zhiwang.zfm.controller.webapp.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

import com.nyd.order.model.common.ChkUtil;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.zeus.api.zzl.ZeusForOrderPayBackServise;
import com.nyd.zeus.model.BillRemindFlowReq;
import com.nyd.zeus.model.BillRemindFlowVo;
import com.zhiwang.zfm.common.response.bean.sys.UserVO;
import com.zhiwang.zfm.config.shiro.ShiroUtils;

@RestController
@RequestMapping("/api/remindFlow")
@Api(description="提醒")
public class RemindFlowController {

	
	private static Logger LOGGER = LoggerFactory.getLogger(RemindFlowController.class);

	@Autowired
	private ZeusForOrderPayBackServise zeusForOrderPayBackServise;
	
	@PostMapping("/queryRemindMsgList")
	@ApiOperation(value = "查询提醒流水")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<List<BillRemindFlowVo>> getOrderProduct(String orderNo) {
		List<BillRemindFlowVo> list  = zeusForOrderPayBackServise.queryRemindMsgList(orderNo);
		CommonResponse<List<BillRemindFlowVo>> resp = (CommonResponse<List<BillRemindFlowVo>>) new CommonResponse<>().success("操作成功");
		resp.setData(list);
		return resp;
	}
	
	@PostMapping("/addRemindMsg")
	@ApiOperation(value = "添加提醒流水")
	@Produces(value = MediaType.APPLICATION_JSON)
	public CommonResponse<?> addRemindMsg(@RequestBody BillRemindFlowReq req) {
		if(ChkUtil.isEmptys(req.getOrderNo()) || ChkUtil.isEmptys(req.getRemindMsg())){
			return new CommonResponse<>().error("请填写提醒内容");
		}
		UserVO userVO=(UserVO) ShiroUtils.getSessionAttribute(ShiroUtils.EMPLOYEE_SESSION);
		zeusForOrderPayBackServise.insertRemindMsg(req.getOrderNo(), req.getRemindMsg(), userVO.getId(), userVO.getLoginName());
		return new CommonResponse<>().success("操作成功");
	}
	
}
