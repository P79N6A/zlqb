package com.nyd.order.dao;

import com.nyd.order.entity.OrderWentong;
import com.nyd.order.model.OrderWentongInfo;

import java.util.List;

/**
 * Cong Yuxiang
 * 2018/4/24
 **/
public interface OrderWentongDao {
    void save(OrderWentong orderWentong) throws Exception;
    List<OrderWentongInfo> getOrderWTByTime(String startDate, String endDate,String name,String mobile);
}
