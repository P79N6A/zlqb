package com.nyd.order.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuqiu
 */
@Mapper
public interface BalanceOrderMapper {
    /**
     * 设置资产订单号
     * @param orderNo
     * @param userId
     */
    void setOrderNoForJx(@Param("orderNo") String orderNo,@Param("userId")  String userId);
}
