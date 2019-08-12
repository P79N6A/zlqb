package com.nyd.order.api;

import com.nyd.order.model.OrderDetailInfo;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

/**
 * Created by Dengw on 2017/11/13
 */
public interface OrderDetailContract {
    ResponseData<OrderDetailInfo> getOrderDetailByOrderNo(String orderNo);

    ResponseData<List<OrderDetailInfo>> getOrderDetailsByUserId(String userId);

    ResponseData<List<OrderDetailInfo>> getOrderDetailsByIdCardNo(String idNumber);

    ResponseData<List<OrderDetailInfo>> getOrderDetailsByMobile(String mobile);
    
    ResponseData updateClickVip(String orderNo);
}
