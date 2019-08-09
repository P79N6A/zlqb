package com.nyd.admin.dao.mapper;


import com.nyd.admin.entity.RepayReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RepayReportMapper {


    List<RepayReport> selectByTimeRange(Map map);
}