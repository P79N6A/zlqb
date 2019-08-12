package com.nyd.order.dao;

import com.nyd.order.entity.OrderDetail;
import com.nyd.order.model.OrderDetailInfo;

import java.util.List;

/**
 * Created by Dengw on 2017/11/8
 */
public interface OrderDetailDao {
    void save(OrderDetail orderDetail) throws Exception;

    void update(OrderDetailInfo orderDetailInfo) throws Exception;

    List<OrderDetailInfo> getObjectsByOrderNo(String orderNo) throws Exception;

    List<OrderDetailInfo> getObjectsByUserId(String userId) throws Exception;

    List<OrderDetailInfo> getOrderDetailsByIdCardNo(String idNumber) throws Exception;

    List<OrderDetailInfo> getOrderDetailsByMobile(String mobile) throws Exception;
}
