package com.nyd.admin.service.impl;

import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.RemitReportMapper;
import com.nyd.admin.dao.mapper.RepayReportMapper;
import com.nyd.admin.entity.RemitReport;
import com.nyd.admin.entity.RepayReport;
import com.nyd.admin.model.RemitReportVo;
import com.nyd.admin.model.RepayReportVo;
import com.nyd.admin.service.FinanceService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.RemitReportMapStruct;
import com.nyd.admin.service.utils.RepayReportMapStruct;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Cong Yuxiang
 * 2017/12/27
 **/
@Service
public class FinanceServiceImpl implements FinanceService{

    Logger logger = LoggerFactory.getLogger(FinanceServiceImpl.class);

    @Autowired
    private RemitReportMapper remitReportMapper;
    @Autowired
    private RepayReportMapper repayReportMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<RemitReportVo> queryRemitReportVo(String startDate, String endDate) {
        Map map = new HashMap();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(startDate == null){
            map.put("startDate", DateFormatUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"));
        }else {
            try {
                map.put("startDate", DateFormatUtils.format(DateUtils.addDays(simpleDateFormat.parse(startDate),1),"yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("startDate 异常"+e.getMessage());
                map.put("startDate", DateFormatUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"));
            }
        }
        if(endDate==null){
            map.put("endDate", DateFormatUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"));
        }else {
            try {
                map.put("endDate", DateFormatUtils.format(DateUtils.addDays(simpleDateFormat.parse(endDate),1),"yyyy-MM-dd"));
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error("endDate 异常"+e.getMessage());
                map.put("endDate", DateFormatUtils.format(DateUtils.addDays(new Date(),1),"yyyy-MM-dd"));
            }
        }
        List<RemitReport> result = remitReportMapper.selectByTimeRange(map);
        return RemitReportMapStruct.INSTANCE.poList2VoList(result);
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public List<RepayReportVo> queryRepayReportVo(String startDate, String endDate) {
        Map map = new HashMap();
        if(startDate == null){
            map.put("startDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        }else {
            map.put("startDate", startDate);
        }
        if(endDate==null){
            map.put("endDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
        }else {
            map.put("endDate", endDate);
        }
        List<RepayReport> result = repayReportMapper.selectByTimeRange(map);
        return RepayReportMapStruct.INSTANCE.poList2VoList(result);
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(DateFormatUtils.format(DateUtils.addDays(new Date(),-1),"yyyy-MM-dd"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(DateFormatUtils.format(DateUtils.addDays(simpleDateFormat.parse("2017-12-29"),-1),"yyyy-MM-dd"));
    }
}
