package com.nyd.order.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.nyd.order.model.OrderExceptionInfo;
import com.nyd.order.model.OrderInfo;

/**
 * 
 * @author zhangdk
 *
 */
@Repository
public interface OrderExceptionMapper {

    void save(OrderExceptionInfo info) throws Exception;

    void update(OrderExceptionInfo info) throws Exception;
    
    List<OrderExceptionInfo> queryOrderException(Map<String,Object> param) throws Exception;
    
    List<OrderExceptionInfo> getOrderExceptionByOrderNo(OrderInfo info) throws Exception;
    
    Integer queryOrderExceptionCount(Map<String,Object> param) throws Exception;

}
