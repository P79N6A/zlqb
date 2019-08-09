package com.nyd.order.dao.mapper;

import com.nyd.order.entity.OrderWentong;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderWentongMapper {

    List<OrderWentong> selectByTime(Map map);

}