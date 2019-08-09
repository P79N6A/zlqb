package com.nyd.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.dao.mapper.ReconResultMapper;
import com.nyd.admin.entity.ReconResult;
import com.nyd.admin.model.ReconQueryVo;
import com.nyd.admin.model.dto.ReconResultDto;
import com.nyd.admin.service.CrudService;
import com.nyd.admin.service.ReconResultService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.nyd.admin.service.utils.ReconResultMapStruct;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cong Yuxiang
 * 2017/12/7
 **/
@Service
public class ReconResultServiceImpl  extends CrudService<ReconResultMapper,ReconResult> implements ReconResultService {

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ZEUS)
    @Override
    public PageInfo<ReconResultDto> findPage(ReconQueryVo vo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ReconResult reconResult = new ReconResult();
        reconResult.setOrderNo(vo.getOrderNo());
        if(vo.getEndDate() == null){
            vo.setEndDate(sdf.format(new Date()));
        }else{
            reconResult.setEndDate(sdf.parse(vo.getEndDate()));
        }
        reconResult.setStartDate(sdf.parse(vo.getStartDate()));
        reconResult.setFundCode(vo.getFundCode());
        reconResult.setResultCode(vo.getResultCode());

        PageInfo<ReconResult> result = this.findPage(vo,reconResult);
        return ReconResultMapStruct.INSTANCE.poPage2DtoPage(result);
    }
}
