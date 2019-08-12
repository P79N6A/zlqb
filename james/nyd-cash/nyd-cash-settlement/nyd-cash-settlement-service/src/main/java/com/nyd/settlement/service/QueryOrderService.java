package com.nyd.settlement.service;

import com.github.pagehelper.PageInfo;
import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.vo.QueryOrderVo;
import java.text.ParseException;

/**
 * @author Peng
 * @create 2018-01-15 16:19
 **/
public interface QueryOrderService {
    PageInfo<QueryOrderVo> findPage (QueryDto dto) throws ParseException;

    PageInfo<QueryOrderVo> waitOrder (QueryDto dto) throws ParseException;
}
