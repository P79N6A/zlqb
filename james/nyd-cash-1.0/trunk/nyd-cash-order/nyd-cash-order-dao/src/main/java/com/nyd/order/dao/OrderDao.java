package com.nyd.order.dao;

import com.nyd.order.entity.Order;
import com.nyd.order.entity.OrderReviewed;
import com.nyd.order.model.OrderCheckQuery;
import com.nyd.order.model.OrderInfo;

import java.util.List;

/**
 * Created by Dengw on 2017/11/8.
 */
public interface OrderDao {
    void save(Order order) throws Exception;

    void update(OrderInfo orderInfo) throws Exception;

    void updateOrderNo(String orderNo,OrderInfo orderInfo) throws Exception;

    List<OrderInfo> getObjectsByOrderNo(String orderNo) throws Exception;

    List<OrderInfo> getObjectsByUserId(String userId) throws Exception;
    
    List<OrderInfo> getObjectsByUserIdAndAppName(String userId,String appName) throws Exception;

    List<OrderInfo> getLastOrderByUserId(String userId) throws Exception;

    List<OrderInfo> getRepayOrderByUserId(String userId) throws Exception;

    List<OrderInfo> getObjectsByIbankOrderNo(String ibankOrderNo) throws Exception;
    
    List<OrderInfo> getOrderCheckQuery(OrderCheckQuery orderCheckQuery) throws Exception;

    List<OrderInfo> getBorrowInfoByUserId(String userId) throws Exception;

    List<OrderInfo> getBorrowInfoByOrderNo(String orderNo) throws Exception;
    OrderInfo getCheckOrderSuccess(String userId)throws Exception;
    
    public void saveOrderReviewed(OrderReviewed reviewed);
}
