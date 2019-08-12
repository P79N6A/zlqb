package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.TransformReportDao;
import com.nyd.admin.dao.UserDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.TransformReportMapper;
import com.nyd.admin.entity.TransformReport;
import com.nyd.admin.model.TransformChartVo;
import com.nyd.admin.model.TransformReportVo;
import com.nyd.admin.model.dto.TransformReportDto;
import com.nyd.admin.model.enums.UserType;
import com.nyd.admin.model.power.vo.UserVo;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.TransformReportService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.TransformReportStruct;
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

/**
 * 2017-12-13
 */
@Service
public class TransformReportServiceImpl extends CrudService<TransformReportMapper,TransformReport> implements TransformReportService {

    private static Logger LOGGER = LoggerFactory.getLogger(TransformReportServiceImpl.class);
    @Autowired
    private TransformReportDao transformReportDao;
    @Autowired
    UserDao userDao;
    @Autowired
    private TransformReportMapper transformReportMapper;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public ResponseData<TransformReportVo> getTransformInfoForDate(String date) {
        ResponseData responseData = ResponseData.success();
        try {
            List<TransformReportVo> transformReportList = transformReportDao.getTransformReportByDate(date);
            responseData.setData(transformReportList);
        } catch (Exception e) {
            LOGGER.error("query transformReport error! date = "+date,e);
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public ResponseData getTransformInfoForSource(String source) {
        ResponseData responseData = ResponseData.success();
        try {
            List<TransformReportVo> transformReportList = transformReportDao.getTransformReportBySource(source);
            responseData.setData(transformReportList);
        } catch (Exception e) {
            LOGGER.error("query transformReport error! date = "+source,e);
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData getTransformInfo(TransformChartVo vo){
        ResponseData responseData = ResponseData.success();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            TransformReport transform = new TransformReport();
            if(vo.getEndDate() == null){
                transform.setEndDate(new Date());
            }else{
                transform.setEndDate(sdf.parse(vo.getEndDate()));
            }
            //前端暂时不会传 开始时间和结束时间(默认查询一周的数据)
            if(vo.getStartDate() == null){
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE,-6);
                transform.setStartDate(c.getTime());
            }
            transform.setSource(vo.getSource());
            List<TransformChartVo> transformChartVoList = transformReportMapper.findForChart(transform);
            responseData.setData(transformChartVoList);
        }catch (Exception e) {
            LOGGER.error("query transformReport error!",e);
        }
        return responseData;
    }

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public PageInfo<TransformReportDto> findPage(TransformReportVo vo,String accountNo) throws Exception {

        //根据accountNo查询是内部还是外部用户
        UserVo userVo = new UserVo();
        userVo.setAccountNo(accountNo);
        List<UserVo> userVoList = userDao.getUserInfo(userVo);
        if (userVoList!=null&&userVoList.size()>0) {
            userVo = userVoList.get(0);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TransformReport transform = new TransformReport();
        if(vo.getEndDate() == null){
            transform.setEndDate(new Date());
        }else{
            transform.setEndDate(sdf.parse(vo.getEndDate()));
        }
        if(vo.getStartDate() != null){
            transform.setStartDate(sdf.parse(vo.getStartDate()));
        }
        if (UserType.OUTSIDE.getCode().equals(userVo.getUserType())) { //外部用户只能查询自己本人
            transform.setSource(accountNo);
        }else{
            transform.setSource(vo.getSource());
        }
        PageInfo<TransformReport> transformReportPageInfo = this.findPage(vo,transform);
        return TransformReportStruct.NSTANCE.poPage2VoPage(transformReportPageInfo);
    }

}
