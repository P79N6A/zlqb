package com.nyd.order.api.zzl;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.nyd.order.entity.Order;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderCheckVo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.OrderRecordHisVo;
import com.nyd.order.model.UserNoParam;
import com.nyd.order.model.common.CommonResponse;
import com.nyd.order.model.common.PagedResponse;
import com.nyd.order.model.vo.ExamReq;

public interface OrderForZLQServise {
	public PagedResponse<List<OrderCheckVo>> findByParam(OrderCheckQuery orderCheckQuery) throws Exception;
	public List<OrderRecordHisVo> findHisByUserId(UserNoParam user);
	

	public Order  findManagerFee(OrderInfo order);
	
	//查询审核通过的记录
	public Boolean findOrderStatus(OrderInfo orderInfo);

	public CommonResponse  checkApproval(ExamReq req);
	
	public CommonResponse<Boolean> check(String orderId);
	
	public com.nyd.order.model.common.CommonResponse<List<ExamReq>> queryProduct(ExamReq req);
	

}

