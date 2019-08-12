package com.nyd.settlement.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.CostQueryMapper;
import com.nyd.settlement.model.dto.RecommendFeeQueryDto;
import com.nyd.settlement.model.dto.ValuationFeeQueryDto;
import com.nyd.settlement.model.po.RecommendFeePo;
import com.nyd.settlement.model.po.ValuationFeePo;
import com.nyd.settlement.service.CostQueryService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.RecommendFeeQueryStruct;
import com.nyd.settlement.service.struct.ValuationFeeQueryStruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CostQueryServiceImpl implements CostQueryService {
    private static Logger LOGGER = LoggerFactory.getLogger(CostQueryServiceImpl.class);

    @Autowired
    private CostQueryMapper costQueryMapper;

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ORDER)
    @Override
    public PageInfo queryValuationFee(ValuationFeeQueryDto dto) {
        if (StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate(null);
        }
        if (StringUtils.isBlank(dto.getStartDate())) {
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<ValuationFeePo> list = costQueryMapper.findValuationFee(dto);
        return ValuationFeeQueryStruct.INSTANCE.poPage2VoPage(new PageInfo(list));
    }

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ORDER)
    @Override
    public PageInfo queryRecommendFee(RecommendFeeQueryDto dto) {
        if (StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate(null);
        }
        if (StringUtils.isBlank(dto.getStartDate())) {
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<RecommendFeePo> list = costQueryMapper.findRecommendFee(dto);
        for (RecommendFeePo po: list){
            BigDecimal recommendFee = po.getRecommendFee();//推荐费
            BigDecimal refundAmount = po.getRefundAmount();//已退金额
            //剩余可退金额 = 推荐费 - 已退金额
            BigDecimal result = recommendFee.subtract(refundAmount);
            po.setResultRefundAmount(result);

        }
        return RecommendFeeQueryStruct.INSTANCE.poPage2VoPage(new PageInfo(list));
    }


}
