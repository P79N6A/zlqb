package com.nyd.order.api;

import com.nyd.order.entity.BalanceOrder;
import com.nyd.order.model.CapitalOrderRelationInfo;
import com.nyd.order.model.OrderInfo;
import com.nyd.order.model.dto.CapitalOrderRelationDto;
import com.nyd.order.model.dto.OrderQcgzDto;
import com.tasfe.framework.support.model.ResponseData;

import java.util.List;

public interface CapitalOrderRelationContract {

    /**
     * 保存渠道资产和侬要贷订单关联关系
     */
    ResponseData saveCapitalOrderRelation(CapitalOrderRelationInfo capitalOrderRelationInfo);


    /**
     * 根据资产编号找到对应的渠道信息
     */
    ResponseData<CapitalOrderRelationDto> getCapitalOrderRelation(String assetId);


    /**
     * 根据订单编号查询资产编号等信息
     */
    ResponseData<OrderQcgzDto> selectAssetNo(String orderNo);

    /**
     * 根据资产编号查询订单编号等信息
     */
    ResponseData<OrderInfo> selectOrderInfo(String assetNo);

    /**
     * 保存资产订单
     */
    ResponseData saveBalanceOrder(BalanceOrder balanceOrder);

    ResponseData setOrderNoForJx(String orderNo, String userId);

    ResponseData<List<OrderInfo>> selectOrderInfos(List<String> assetIds);

    ResponseData updateFundCode(String orderNo);

    /**
     * 查询当天即信需要还款的订单
     * @return
     */
    ResponseData selectOrderInfosFromJx();
}
