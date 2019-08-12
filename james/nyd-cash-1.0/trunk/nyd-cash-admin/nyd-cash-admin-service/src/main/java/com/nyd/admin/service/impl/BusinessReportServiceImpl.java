package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.BusinessReportMapper;
import com.nyd.admin.entity.BusinessReport;
import com.nyd.admin.model.BusinessChartVo;
import com.nyd.admin.model.BusinessReportVo;
import com.nyd.admin.model.dto.BusinessReportDto;
import com.nyd.admin.service.BusinessReportService;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.BusinessReportStruct;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BusinessReportServiceImpl extends CrudService<BusinessReportMapper,BusinessReport> implements BusinessReportService {

    private static Logger LOGGER = LoggerFactory.getLogger(BusinessReportServiceImpl.class);
    @Autowired
    BusinessReportMapper businessReportMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public PageInfo<BusinessReportDto> findPage(BusinessReportVo vo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BusinessReport businessReport = new BusinessReport();
        if (vo.getEndDate() == null) {
            businessReport.setEndDate(new Date());
        } else {
            businessReport.setEndDate(sdf.parse(vo.getEndDate()));
        }
        if (vo.getStartDate() != null) {
            businessReport.setStartDate(sdf.parse(vo.getStartDate()));
        }
        PageInfo<BusinessReport> businessReportPageInfo = this.findPage(vo, businessReport);
        return BusinessReportStruct.NSTANCE.poPage2VoPage(businessReportPageInfo);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getBusinessChart(BusinessChartVo vo) {
        BusinessReport businessReport = new BusinessReport();
        ResponseData responseData = ResponseData.success();
        //前端暂时不会传 开始时间和结束时间(默认查询15天的数据)
        if (vo.getEndDate() == null) {
            businessReport.setEndDate(new Date());
        }
        if (vo.getStartDate() == null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -14);
            businessReport.setStartDate(c.getTime());
        }
        List<BusinessChartVo> businessChartVoList = businessReportMapper.businessChart(businessReport);
        responseData.setData(businessChartVoList);
        return responseData;
    }
}

