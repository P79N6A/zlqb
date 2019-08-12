package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.FailReportMapper;
import com.nyd.admin.entity.FailReport;
import com.nyd.admin.model.FailReportVo;
import com.nyd.admin.model.dto.FailReportDto;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.FailReportService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.FailReportStruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Peng
 * @create 2017-12-14 22:24
 **/
@Service
public class FailReportServiceImpl extends CrudService<FailReportMapper,FailReport> implements FailReportService{

    private static Logger LOGGER = LoggerFactory.getLogger(FailReportServiceImpl.class);

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public PageInfo<FailReportDto> findPage(FailReportVo vo) throws ParseException {
        SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
        FailReport failReport = new FailReport();
        if(vo.getEndDate() == null){
            failReport.setEndDate(new Date());
        }else{
            failReport.setEndDate(sdf.parse(vo.getEndDate()));
        }
        if(vo.getStartDate() != null){
            failReport.setStartDate(sdf.parse(vo.getStartDate()));
        }
        PageInfo<FailReport> failReportPageInfo = this.findPage(vo,failReport);
        return FailReportStruct.NSTANCE.poPage2VoPage(failReportPageInfo);
    }
}
