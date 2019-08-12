package com.nyd.order.dao;

import com.nyd.order.entity.CapitalOrderRelation;
import com.nyd.order.model.dto.CapitalOrderRelationDto;

import java.util.List;


public interface CapitalOrderRelationDao {
    /**
     * 渠道资产和侬要贷订单关联关系
     * @param capitalOrderRelation
     * @throws Exception
     */
    void save(CapitalOrderRelation capitalOrderRelation) throws Exception;

    List<CapitalOrderRelationDto> getCapitalOrderRelationByAssetId(String assetId) throws Exception;
}
