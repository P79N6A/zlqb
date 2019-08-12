package com.nyd.order.dao;

import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.entity.OrderWentong;
import com.nyd.order.model.OrderWentongInfo;

import java.util.List;

/**
 * @author liuqiu
 */
public interface BalanceOrderDao {
    void save(BalanceOrder balanceOrder) throws Exception;

}
