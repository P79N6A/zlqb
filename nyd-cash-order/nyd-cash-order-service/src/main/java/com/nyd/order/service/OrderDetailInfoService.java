package com.nyd.order.service;

import com.nyd.order.model.OrderDetailInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * Created by Dengw on 2017/11/8
 */
public interface OrderDetailInfoService {
    ResponseData<OrderDetailInfo> getOrderDetailByOrderNo(String orderNo);
}
