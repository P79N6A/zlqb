package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.RemitReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RemitReportMapper {
    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insert(RemitReport record);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insertSelective(RemitReport record);
    void deleteRemitReport(Map map);
}