package com.nyd.order.service;

import com.nyd.order.model.OrderStatusLogInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by dengw on 2017/11/9.
 */
public interface OrderStatusLogService {
    ResponseData<List<OrderStatusLogInfo>> getOrderStatusLogByOrderNo(String orderNo);
}
