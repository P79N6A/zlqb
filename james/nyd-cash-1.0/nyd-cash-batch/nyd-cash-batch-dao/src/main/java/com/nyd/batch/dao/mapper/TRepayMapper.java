package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.TRepay;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TRepayMapper {



    List<TRepay> queryByTimeRange(Map map);


}