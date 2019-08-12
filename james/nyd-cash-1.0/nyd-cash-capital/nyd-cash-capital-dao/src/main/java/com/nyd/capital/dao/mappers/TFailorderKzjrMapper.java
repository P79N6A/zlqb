package com.nyd.capital.dao.mappers;

import com.nyd.capital.dao.CrudDao;
import com.nyd.capital.entity.FailOrderKzjr;
import com.nyd.capital.entity.TimeRangeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TFailorderKzjrMapper extends CrudDao<FailOrderKzjr> {
        List<FailOrderKzjr> queryByTime(TimeRangeVo vo);
}