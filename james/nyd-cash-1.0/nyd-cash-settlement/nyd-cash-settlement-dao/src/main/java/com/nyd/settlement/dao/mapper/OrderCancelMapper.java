package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.model.dto.OrderCancelDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Peng
 * @create 2018-01-17 11:20
 **/
@Mapper
public interface OrderCancelMapper {
    String queryOrderStatus(OrderCancelDto dto);
}
