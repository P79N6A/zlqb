package com.nyd.order.service.impl.zzl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nyd.order.api.zzl.OrderForOrderPayBackServise;
import com.nyd.order.api.zzl.OrderSqlService;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.service.NewOrderInfoService;
import com.nyd.user.entity.User;
import com.tasfe.framework.support.model.ResponseData;

@Service("orderForOrderPayBackServise")
public class OrderForOrderPayBackServiseImpl implements OrderForOrderPayBackServise{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderForOrderPayBackServiseImpl.class);

	@Autowired
	private OrderSqlService orderSqlService;
	
	@Autowired
    private NewOrderInfoService orderInfoService;

	@Override
	public ResponseData<OrderInfo> checkOrderSuccess(String userId) {
		User user = new User();
		user.setUserId(userId);
		return orderInfoService.checkOrderSuccess(user);
	}
}
