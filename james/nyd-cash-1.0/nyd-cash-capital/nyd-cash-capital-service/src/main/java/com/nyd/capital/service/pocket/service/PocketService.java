package com.nyd.capital.service.pocket.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.nyd.capital.model.pocket.*;
import com.nyd.order.model.OrderInfo;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author liuqiu
 */
public interface PocketService {

    /**
     * 口袋理财放款接口
     * @param  orderInfo
     * @return
     */
    ResponseData withdraw(OrderInfo orderInfo);


    /**
     * 口袋理财放款查询
     * @param request
     * @return
     */
//    ResponseData withdrawQuery(PocketWithdrawQueryRequest request);
    ResponseData withdrawQuery(String orderNo);
    
    /**
     * 口袋理财创建订单
     * @param request
     * @return
     */
    ResponseData createOrder(OrderInfo request);


    /**
     * 口袋理财订单查询
     * @param request
     * @return
     */
    ResponseData queryOrder(OrderInfo request);
    
    /**
     * 口袋理财放款回调
     * @param pocketCallbackDto
     * @return
     */
    PocketCallbackResponseData pocketCallback(@RequestBody PocketCallbackDto pocketCallbackDto);
}
