package com.nyd.settlement.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.QueryOrderMapper;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.po.QueryOrderPo;
import com.nyd.settlement.model.vo.QueryOrderVo;
import com.nyd.settlement.service.QueryOrderService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.QueryOrderStruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * @author Peng
 * @create 2018-01-15 16:22
 **/
@Service
public class QueryOrderServiceImpl implements QueryOrderService {
    @Autowired
    QueryOrderMapper queryOrderMapper;
    private static Logger LOGGER = LoggerFactory.getLogger(QueryOrderServiceImpl.class);
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ORDER)
    @Override
    public PageInfo<QueryOrderVo> findPage(QueryDto dto) throws ParseException {
        if (StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate(null);
        }
        if (StringUtils.isBlank(dto.getStartDate())) {
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<QueryOrderPo> failReportPageInfo = queryOrderMapper.queryOrderList(dto);
        return QueryOrderStruct.INSTANCE.poPage2VoPage(new PageInfo(failReportPageInfo));
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_MYSQL)
    @Override
    public PageInfo<QueryOrderVo> waitOrder(QueryDto dto) throws ParseException {
        if (StringUtils.isNotBlank(dto.getEndDate())) {
            dto.setEndDate(dto.getEndDate());
        }else {
            dto.setEndDate(null);
        }
        if (StringUtils.isNotBlank(dto.getStartDate())) {
            dto.setStartDate(dto.getStartDate());
        } else{
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<QueryOrderPo> failReportPageInfo = queryOrderMapper.queryWaitOrderList(dto);
        return QueryOrderStruct.INSTANCE.poPage2VoPage(new PageInfo(failReportPageInfo));
    }
}
