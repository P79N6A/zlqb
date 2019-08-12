package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.TRemit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TRemitMapper {


    List<TRemit> queryByTimeRange(Map map);


}