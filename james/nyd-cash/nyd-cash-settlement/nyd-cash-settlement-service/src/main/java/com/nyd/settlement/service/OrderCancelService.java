package com.nyd.settlement.service;

import com.nyd.settlement.model.dto.OrderCancelDto;
import com.nyd.settlement.model.dto.QueryDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author Peng
 * @create 2018-01-16 11:44
 **/
public interface OrderCancelService {
    ResponseData getOrderCancel(QueryDto dto) ;

    ResponseData cancelOrder(OrderCancelDto dto) ;
}
