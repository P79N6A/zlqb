package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.TMemberLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TMemberLogMapper {

    List<TMemberLog> selectByTimeRange(Map map);
    List<TMemberLog> selectByUserId(Map map);
    List<TMemberLog> selectByOrderNo(Map map);
    List<TMemberLog> selectByMemberId(Map map);

}