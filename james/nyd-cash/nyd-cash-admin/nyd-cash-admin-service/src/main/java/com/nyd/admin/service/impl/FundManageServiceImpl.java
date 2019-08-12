package com.nyd.admin.service.impl;

import com.nyd.admin.dao.FundConfigDao;
import com.nyd.admin.dao.ds.DataSourceContextHolder;
import com.nyd.admin.model.dto.FundCreateDto;
import com.nyd.admin.model.fundManageModel.FundConfigModel;
import com.nyd.admin.service.FundBussinessService;
import com.nyd.admin.service.FundManageService;
import com.nyd.admin.service.aspect.RoutingDataSource;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hwei on 2017/12/29.
 */
@Service
public class FundManageServiceImpl implements FundManageService{
    private static Logger logger = LoggerFactory.getLogger(FundManageServiceImpl.class);

    @Autowired
    FundConfigDao fundConfigDao;
    @Autowired
    FundBussinessService fundBussinessService;

    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData queryFundConfig() {
        ResponseData responseData = ResponseData.success();
        try {
            List<FundConfigModel> list = fundConfigDao.getFundConfig();
            responseData.setData(list);
        } catch (Exception e) {
            logger.error("getFundConfig has exception!",e);
            responseData = ResponseData.error("服务器开小差了");
        }
        return responseData;
    }

    /**
     * 创建资产
     * @param fundCreateDto
     * @return
     */
    @RoutingDataSource(DataSourceContextHolder.DATA_SOURCE_ADMIN)
    @Override
    public ResponseData createFundInfo(FundCreateDto fundCreateDto) throws Exception {
        ResponseData responseData = ResponseData.success();
        //参数校验
        if (!verify(fundCreateDto)) {
            return ResponseData.error("参数不能为空");
        }
        return fundBussinessService.createFundInfo(fundCreateDto);
    }

    public boolean verify(FundCreateDto fundCreateDto){
       if (fundCreateDto==null){
           return false;
       }
       if (StringUtils.isBlank(fundCreateDto.getName())||StringUtils.isBlank(fundCreateDto.getIdNumber())) {
           return false;
       }
       if (fundCreateDto.getInvestmentAmount()==null||fundCreateDto.getFundDetailDtoList()==null||fundCreateDto.getBackAmount()==null) {
           return false;
       }
       return true;
    }

}
