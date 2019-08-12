package com.nyd.settlement.dao;

import com.nyd.settlement.model.dto.OrderCancelDto;
import com.nyd.settlement.model.dto.OrderDto;
import com.nyd.settlement.model.dto.OrderStatusLogDto;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.vo.OrderCancelVo;
import java.util.List;

/**
 * @author Peng
 * @create 2018-01-16 10:46
 **/
public interface OrderCancelDao {
    List<OrderCancelVo> getOrderCancel(QueryDto dto) throws Exception;

    void save(OrderCancelDto dto) throws Exception;

    void updateOrder(OrderDto dto) throws Exception;

    void deleteFailKzjr(String orderNo) throws Exception;

    void saveOrderStatusLog(OrderStatusLogDto dto) throws Exception;
}
