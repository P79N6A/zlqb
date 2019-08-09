package com.nyd.order.api;

import com.nyd.order.model.OrderStatusLogInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface OrderStatusLogContract {
    ResponseData<List<OrderStatusLogInfo>> getOrderStatusLogByOrderNo(String orderNo);
}
