package com.nyd.admin.dao.mapper;

import com.nyd.admin.dao.CrudDao;
import com.nyd.admin.entity.BusinessReport;
import com.nyd.admin.model.BusinessChartVo;

import java.util.List;

public interface BusinessReportMapper extends CrudDao<BusinessReport> {
    List<BusinessChartVo> businessChart(BusinessReport businessReport);
}
