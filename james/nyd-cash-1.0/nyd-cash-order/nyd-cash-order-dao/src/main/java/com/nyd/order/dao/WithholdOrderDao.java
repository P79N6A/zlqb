package com.nyd.order.dao;

import com.nyd.order.entity.WithholdOrder;
import com.nyd.order.model.OrderInfo;

import java.util.List;

/**
 * @author liuqiu
 */
public interface WithholdOrderDao {

    void save(WithholdOrder order) throws Exception;

    void update(WithholdOrder orderInfo) throws Exception;

    List<WithholdOrder> getObjectsByMemberId(String memberId) throws Exception;

    List<WithholdOrder> getObjectsPayOrderNo(String payOrderNo) throws Exception;
}
