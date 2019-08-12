package com.nyd.admin.dao.mapper;


import com.nyd.admin.entity.RemitReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RemitReportMapper {

    List<RemitReport> selectByTimeRange(Map map);


}