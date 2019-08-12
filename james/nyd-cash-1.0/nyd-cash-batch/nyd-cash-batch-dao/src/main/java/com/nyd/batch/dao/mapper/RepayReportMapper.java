package com.nyd.batch.dao.mapper;


import com.nyd.batch.entity.RepayReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RepayReportMapper {
    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insert(RepayReport record);

    /**
     *
     * @mbg.generated 2017-12-25
     */
    int insertSelective(RepayReport record);

    void deleteRepayReport(Map map);
}