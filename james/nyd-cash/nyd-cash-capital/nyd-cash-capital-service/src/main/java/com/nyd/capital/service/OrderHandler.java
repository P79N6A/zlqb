package com.nyd.capital.service;

import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * 
 * @author zhangdk
 *
 */
public interface OrderHandler {
    /**
     *  订单成功处理
     * @return
     */
    ResponseData orderSuccessHandler(OrderInfo orderInfo);

    /**
     * 订单失败处理
     */
    ResponseData orderFailHankdler(OrderInfo orderInfo, String channalKey);
    /**
     * 订单失败处理
     */
    ResponseData orderFailHankdler(OrderInfo orderInfo, String channalKey, ResponseData data);

}
