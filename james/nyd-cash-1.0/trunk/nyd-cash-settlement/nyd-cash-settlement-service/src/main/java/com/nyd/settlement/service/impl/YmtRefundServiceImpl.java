package com.nyd.settlement.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nyd.settlement.api.YmtRefundServiceApi;
import com.nyd.settlement.dao.ds.DataSourceContextHolder;
import com.nyd.settlement.dao.mapper.YmtRefundMapper;
import com.nyd.settlement.entity.refund.YmtRefund;
import com.nyd.settlement.model.dto.RecommendRefundDto;
import com.nyd.settlement.model.dto.RefundDetailDto;
import com.nyd.settlement.model.po.YmtRefundPo;
import com.nyd.settlement.service.YmtRefundService;
import com.nyd.settlement.service.aspect.RoutingDataSource;
import com.nyd.settlement.service.struct.RecommendRefundAddStruct;
import com.nyd.settlement.service.struct.RefundDetailQueryStruct;
import com.nyd.settlement.service.utils.ValidateUtil;
import com.tasfe.framework.support.model.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YmtRefundServiceImpl implements YmtRefundService,YmtRefundServiceApi {
    private static Logger LOGGER = LoggerFactory.getLogger(YmtRefundServiceImpl.class);

    @Autowired
    private YmtRefundMapper ymtRefundMapper;

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ZEUS)
    @Override
    public PageInfo findRefundDetail(RefundDetailDto dto) {
        if (StringUtils.isBlank(dto.getEndDate())) {
            dto.setEndDate(null);
        }
        if (StringUtils.isBlank(dto.getStartDate())) {
            dto.setStartDate(null);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderBy());
        List<YmtRefundPo> list = ymtRefundMapper.selectRefundDetail(dto);
        return RefundDetailQueryStruct.INSTANCE.poPage2VoPage(new PageInfo(list));
    }

    @RoutingDataSource(DataSourceContextHolder.YMT_DATA_SOURCE_ZEUS)
    @Override
    public ResponseData addRecommendRefund(RecommendRefundDto dto) {
        ResponseData responseData = ResponseData.success();
        try {
            //参数校验
            ValidateUtil.process(dto);
        } catch (Exception e) {
            LOGGER.error("参数不能为空！",e);
            return ResponseData.error("参数不能为空");
        }

        try {
            YmtRefund ymtRefund = RecommendRefundAddStruct.INSTANCE.po2Vo(dto);
            LOGGER.info("记录推荐费退款相关参数："+ JSON.toJSONString(ymtRefund));
            ymtRefundMapper.saveRecommendRefund(ymtRefund);
            return responseData;

        }catch (Exception e){
            LOGGER.error("记录推荐费退款信息异常",e);
            return ResponseData.error("服务器开小差了");
        }


    }

    @Override
    public YmtRefund findByRefundFlowNo(String repayNo) {
        return ymtRefundMapper.selectByRefundFlowNo(repayNo);
    }

}
