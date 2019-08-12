package com.zhiwang.zfm.controller.webapp.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nyd.order.api.zzl.OrderForZLQServise;
import com.nyd.order.model.common.CommonResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/order/report")
@Api(description="判断订单状态")
public class OrderJudgeController {

	@Autowired
	private OrderForZLQServise orderForZLQServise;
	
	/**
	 * data返回true 无审批报告
	 * @param orderNo
	 * @return
	 */
	@ApiOperation(value = "判断订单是否有批核报告")
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public CommonResponse<Boolean> check(@RequestBody String orderNo) {
		CommonResponse<Boolean> common = new CommonResponse<Boolean>();
		common = orderForZLQServise.check(orderNo);
		return common;
	}

}
