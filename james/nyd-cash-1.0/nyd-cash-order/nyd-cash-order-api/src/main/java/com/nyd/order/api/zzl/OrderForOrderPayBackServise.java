package com.nyd.order.api.zzl;

import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;


public interface OrderForOrderPayBackServise {
	
	/**
	 * 根据用户id复用查询出订单号
	 * @param userId
	 * @return
	 */
	public ResponseData<OrderInfo> checkOrderSuccess(String userId);

}

