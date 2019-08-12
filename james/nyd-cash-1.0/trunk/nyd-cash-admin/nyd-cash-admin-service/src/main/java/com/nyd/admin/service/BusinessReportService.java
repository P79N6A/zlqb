package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.BusinessChartVo;
import com.nyd.admin.model.BusinessReportVo;
import com.nyd.admin.model.dto.BusinessReportDto;
import com.tasfe.framework.support.model.ResponseData;

import java.text.ParseException;

public interface BusinessReportService {
    PageInfo<BusinessReportDto> findPage (BusinessReportVo vo) throws ParseException;

    ResponseData getBusinessChart(BusinessChartVo businessChartVo);
}
