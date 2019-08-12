package com.nyd.order.service;

import java.util.List;
import java.util.Map;

import com.nyd.order.model.OrderExceptionInfo;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface OrderExceptionService {
	ResponseData save(OrderExceptionInfo info);
	ResponseData update(OrderExceptionInfo info);
	ResponseData saveByOrderInfo(OrderInfo info);
    ResponseData<OrderExceptionInfo> queryOrderException(Map<String,Object> param);
    List<OrderExceptionInfo> getOrderExceptionByOrderNo(OrderInfo info);
}
