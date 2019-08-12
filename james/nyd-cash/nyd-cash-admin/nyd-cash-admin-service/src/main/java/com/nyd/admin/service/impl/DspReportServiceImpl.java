package com.nyd.admin.service.impl;

import com.nyd.admin.model.DspReportVo;
import com.nyd.admin.model.DspResponseVo;
import com.nyd.admin.model.EnumModel;
import com.nyd.admin.model.enums.DspSourceEnum;
import com.nyd.admin.service.DspReportService;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hwei on 2017/12/18.
 */
@Service
public class DspReportServiceImpl implements DspReportService{
    private static Logger LOGGER = LoggerFactory.getLogger(DspReportServiceImpl.class);

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseData queryDspSucess(DspReportVo vo) {
        ResponseData responseData = ResponseData.success();
        List<DspResponseVo> responseVoList = new ArrayList<>();
        if (isEmpty(vo)){
            //参数为空则默认查询所有数据源当前日期的信息
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Date startDate = parse(date + " 00:00:00");
            Date endDate = new Date();
            List<EnumModel> list = DspSourceEnum.toList();
            for (EnumModel model : list) {
                DspResponseVo dspResponseVo = new DspResponseVo();
                dspResponseVo.setDspDate(date + " 至 " + date);
                dspResponseVo.setDspInterface(model.getName());
                Long dspCount = getDspCount(model.getCode(),startDate,endDate);
                Long dspSuccessCount = getDspSuccessCount(model.getCode(),startDate,endDate);
                dspResponseVo.setCallCount(dspCount);
                dspResponseVo.setSuccessCallCount(dspSuccessCount);
                dspResponseVo.setSuccessRate(getRate(dspCount,dspSuccessCount));
                responseVoList.add(dspResponseVo);
            }
            responseData.setData(responseVoList);
            return responseData;
        }
        if (StringUtils.isBlank(vo.getDspInterface())) {
            //供应商为空，则根据日期进行查询
            Date startDate = parse(vo.getStartDate()+" 00:00:00");
            Date endDate = parse(vo.getEndDate()+" 23:59:59");
            List<EnumModel> list = DspSourceEnum.toList();
            for (EnumModel model : list) {
                DspResponseVo dspResponseVo = new DspResponseVo();
                dspResponseVo.setDspDate(vo.getStartDate() + " 至 " + vo.getEndDate());
                dspResponseVo.setDspInterface(model.getName());
                Long dspCount = getDspCount(model.getCode(),startDate,endDate);
                Long dspSuccessCount = getDspSuccessCount(model.getCode(),startDate,endDate);
                dspResponseVo.setCallCount(dspCount);
                dspResponseVo.setSuccessCallCount(dspSuccessCount);
                dspResponseVo.setSuccessRate(getRate(dspCount,dspSuccessCount));
                responseVoList.add(dspResponseVo);
            }
            responseData.setData(responseVoList);
            return responseData;
        } else {
            //选择了供应商，如果没选日期，则默认查询当前天，否则根据日期查询
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Date startDate = null;
            Date endDate = null;
            DspResponseVo dspResponseVo = new DspResponseVo();
            if (StringUtils.isBlank(vo.getStartDate())){
                startDate = parse(date+" 00:00:00");
                endDate = new Date();
                dspResponseVo.setDspDate(date + " 至 " + date);
            } else {
                startDate = parse(vo.getStartDate()+" 00:00:00");
                endDate = parse(vo.getEndDate()+" 23:59:59");
                dspResponseVo.setDspDate(vo.getStartDate() + " 至 " + vo.getEndDate());
            }
            dspResponseVo.setDspInterface(DspSourceEnum.getName(vo.getDspInterface()));
            Long dspCount = getDspCount(vo.getDspInterface(),startDate,endDate);
            Long dspSuccessCount = getDspSuccessCount(vo.getDspInterface(),startDate,endDate);
            dspResponseVo.setCallCount(dspCount);
            dspResponseVo.setSuccessCallCount(dspSuccessCount);
            dspResponseVo.setSuccessRate(getRate(dspCount,dspSuccessCount));
            responseVoList.add(dspResponseVo);
            responseData.setData(responseVoList);
            return responseData;
        }
    }

    private boolean isEmpty(DspReportVo vo) {
        if (vo==null) {
            return true;
        }
        if (StringUtils.isBlank(vo.getStartDate())&&StringUtils.isBlank(vo.getEndDate())&&StringUtils.isBlank(vo.getDspInterface())){
            return true;
        }
        return false;
    }

    /**
     * 查询dsp调用次数
     */
    private Long getDspCount(String interfaceName,Date startDate, Date endDate) {
        try {
            Query query = Query.query(Criteria.where("create_time").gte(startDate).lt(endDate));
            long count = mongoTemplate.count(query,interfaceName);
            return count;
        } catch (Exception e) {
            LOGGER.error(String.format("getDspCount has exception ! interfaceName = {} ,startDate = {},endDate = {} ",interfaceName,startDate,endDate),e);
        }
        return null;
    }

    /**
     *查询dsp成功次数
     */
    private Long getDspSuccessCount(String interfaceName,Date startDate, Date endDate) {
        try {
            Criteria criteria = new Criteria();
            Query query = Query.query(criteria.andOperator(Criteria.where("create_time").gte(startDate).lt(endDate),Criteria.where("successFlag").is(true)));
            long count = mongoTemplate.count(query,interfaceName);
            return count;
        } catch (Exception e) {
            LOGGER.error(String.format("getDspSuccessCount has exception ! interfaceName = {} ,startDate = {},endDate = {} ",interfaceName,startDate,endDate),e);
        }
        return null;
    }

    private Date parse(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date time = format.parse(date);
            return time;
        } catch (ParseException e) {
            LOGGER.error("parse date has exception!",e);
        }
        return null;
    }

    private String getRate(Long a ,Long b) {
        if (a==null||b==null) {
            return null;
        }
        if (a.longValue()!=0l) {
//            DecimalFormat df = new DecimalFormat("0.0000");
            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(2);
            return nt.format((double)b/(double)a);
        }
        return null;
    }
}
