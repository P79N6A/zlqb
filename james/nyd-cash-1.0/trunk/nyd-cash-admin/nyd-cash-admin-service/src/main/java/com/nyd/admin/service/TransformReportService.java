package com.nyd.admin.service;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.model.TransformChartVo;
import com.nyd.admin.model.TransformReportVo;
import com.nyd.admin.model.dto.TransformReportDto;
import com.tasfe.framework.support.model.ResponseData;

import java.text.ParseException;

public interface TransformReportService {
    ResponseData<TransformReportVo> getTransformInfoForDate(String date);

    ResponseData getTransformInfoForSource(String source);

    ResponseData getTransformInfo(TransformChartVo vo) ;

    PageInfo<TransformReportDto> findPage(TransformReportVo vo,String accountNo) throws Exception;

}
