package com.nyd.order.api;

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
public interface OrderExceptionContract {
	ResponseData save(OrderExceptionInfo info);
	ResponseData update(OrderExceptionInfo info);
	ResponseData audit(String orderNo, Integer auditStatu, String fundCode) throws Exception;
	ResponseData saveByOrderInfo(OrderInfo info);
    ResponseData<List<OrderExceptionInfo>> queryOrderException(Map<String,Object> param);
    ResponseData queryOrderExceptionList(Map<String, Object> param);
}
