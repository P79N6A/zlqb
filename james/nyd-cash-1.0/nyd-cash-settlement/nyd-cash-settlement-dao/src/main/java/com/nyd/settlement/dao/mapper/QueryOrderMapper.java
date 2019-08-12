package com.nyd.settlement.dao.mapper;

import com.nyd.settlement.model.dto.QueryDto;
import com.nyd.settlement.model.po.QueryOrderPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author Peng
 * @create 2018-01-15 15:33
 **/
@Mapper
public interface QueryOrderMapper{
    List<QueryOrderPo> queryOrderList(QueryDto dto);

    List<QueryOrderPo> queryWaitOrderList(QueryDto dto);

}
