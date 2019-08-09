package com.nyd.admin.dao;

import com.nyd.admin.model.TransformReportVo;

import java.util.List;

public interface TransformReportDao {
    List<TransformReportVo> getTransformReportByDate(String date) throws Exception;

    List<TransformReportVo> getTransformReportBySource(String source) throws Exception;

    List<TransformReportVo> getTransformReport(TransformReportVo vo ) throws Exception;
}
