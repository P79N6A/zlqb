package com.nyd.order.dao;

import com.nyd.order.entity.OrderStatusLog;
import com.nyd.order.model.OrderStatusLogInfo;

import java.util.List;

/**
 * Created by Dengw on 2017/11/9.
 */
public interface OrderStatusLogDao {

    void save(OrderStatusLog orderStatusLog) throws Exception;

    List<OrderStatusLogInfo> getObjectsByOrderNo(String orderNo) throws Exception;
}
