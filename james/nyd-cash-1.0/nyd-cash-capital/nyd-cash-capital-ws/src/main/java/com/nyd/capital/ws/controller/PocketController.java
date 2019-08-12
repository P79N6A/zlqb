package com.nyd.capital.ws.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ibank.order.api.OrderInfoServiceContract;
import com.nyd.capital.model.pocket.PocketCallbackDto;
import com.nyd.capital.model.pocket.PocketCallbackResponseData;
import com.nyd.capital.model.pocket.PocketWithdrawQueryRequest;
import com.nyd.capital.service.pocket.service.PocketService;
import com.nyd.order.api.OrderContract;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @Author: zhanch
 * @Description: 空中金融批量放款
 * @Date: 20:24 2018/5/14
 */
@RestController
@RequestMapping("/capital/pocket")
public class PocketController {
	
	@Autowired
	private OrderContract orderContract;
	@Autowired
	private PocketService pocketService;
	
	/**
	 * 放款
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/withdraw",produces="application/json",method = RequestMethod.POST,consumes = "application/json")
	public ResponseData withdraw(@RequestBody String orderNo) {
		ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
		return pocketService.withdraw(orderByOrderNo.getData());
	}
	
	/**
	 * 放款查询
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/withdrawQuery",produces="application/json",method = RequestMethod.POST,consumes = "application/json")
	 public ResponseData withdrawQuery(@RequestBody String orderNo) {
		return pocketService.withdrawQuery(orderNo);
	}
	
	/**
	 * 创建订单
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/createOrder",method = RequestMethod.POST,consumes = "application/json")
	public ResponseData createOrder(@RequestBody String orderNo) {
		ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
		ResponseData responseData = pocketService.createOrder(orderByOrderNo.getData());
		return responseData;
	}
	
	/**
	 * 查询订单
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/queryOrder",produces="application/json",method = RequestMethod.POST,consumes = "application/json")
	public ResponseData queryOrder(@RequestBody String orderNo) {
		ResponseData<OrderInfo> orderByOrderNo = orderContract.getOrderByOrderNo(orderNo);
		return pocketService.queryOrder(orderByOrderNo.getData());
	}
	
	@RequestMapping(value="/pocketCallback",produces="application/json",method = RequestMethod.POST,consumes = "application/json")
	public PocketCallbackResponseData pocketCallback(@RequestBody PocketCallbackDto pocketCallbackDto) {
		return pocketService.pocketCallback(pocketCallbackDto);
	}
	
}
