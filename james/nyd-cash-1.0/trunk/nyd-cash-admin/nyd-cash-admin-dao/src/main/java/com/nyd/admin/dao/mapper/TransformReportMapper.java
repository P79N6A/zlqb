package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.TransformReport;
import com.nyd.admin.model.TransformChartVo;
import java.util.List;

public interface TransformReportMapper extends CrudDao<TransformReport> {
    List<TransformChartVo> findForChart(TransformReport transformReport);
}
