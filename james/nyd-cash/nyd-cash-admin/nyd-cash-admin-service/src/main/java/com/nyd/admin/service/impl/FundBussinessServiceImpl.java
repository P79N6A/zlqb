package com.nyd.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nyd.admin.dao.FundDetailDao;
import com.nyd.admin.dao.FundInfoDao;
import com.nyd.admin.entity.FundDetail;
import com.nyd.admin.entity.FundInfo;
import com.nyd.admin.model.dto.FundCreateDto;
import com.nyd.admin.model.dto.FundDetailDto;
import com.nyd.admin.service.FundBussinessService;
import com.tasfe.framework.support.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hwei on 2018/1/2.
 */
@Service
public class FundBussinessServiceImpl implements FundBussinessService {
    private static Logger logger = LoggerFactory.getLogger(FundBussinessServiceImpl.class);

    @Autowired
    FundInfoDao fundInfoDao;
    @Autowired
    FundDetailDao fundDetailDao;

    /**
     * 因多数据源跟事物之间存在冲突，所以另外加了一层调用
     * @param fundCreateDto
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    @Override
    public ResponseData createFundInfo(FundCreateDto fundCreateDto) throws Exception {
        ResponseData responseData = ResponseData.success();
        //保存fundInfo
        FundInfo fundInfo = new FundInfo();
        BeanUtils.copyProperties(fundCreateDto,fundInfo);
        String fundId = UUID.randomUUID().toString().replaceAll("-", "");
        fundInfo.setFundId(fundId);
        try {
            fundInfoDao.saveFundInfo(fundInfo);
        } catch (Exception e) {
            logger.error("createFundInfo save fundInfo has exception ,fundCreateDto is "+ JSONObject.toJSONString(fundCreateDto),e);
            throw e;
        }
        //保存fundDetail
        List<FundDetail> list = new ArrayList<>();
        List<FundDetailDto> fundDetailDtoList = fundCreateDto.getFundDetailDtoList();
        BigDecimal lastAmount = new BigDecimal(0);
        if (fundDetailDtoList!=null&&fundDetailDtoList.size()>0) {
            for (int i=0;i<fundDetailDtoList.size();i++) {
                FundDetail fundDetail = new FundDetail();
                fundDetail.setFundId(fundId);
                fundDetail.setName(fundCreateDto.getName());
                fundDetail.setIdNumber(fundCreateDto.getIdNumber());
                if (i==0) {
                    fundDetail.setInvestmentAmount(fundCreateDto.getInvestmentAmount());
                    fundDetail.setExpiryDate(getAddMounth(fundCreateDto.getAccountDate(),fundDetailDtoList.get(i).getInvestmentTerm()));
                    lastAmount = fundCreateDto.getInvestmentAmount();
                } else {
                    if (0==fundDetailDtoList.get(i-1).getContinueFlag()) { //是续投
                        if (0==fundDetailDtoList.get(i-1).getContinueType()) { //续投方式是 本金+利息
                            fundDetail.setInvestmentAmount(lastAmount.add(fundDetailDtoList.get(i-1).getExpiryProfit()));
                            lastAmount = lastAmount.add(fundDetailDtoList.get(i-1).getExpiryProfit());
                        } else { // 续投方式 是  本金
                            fundDetail.setInvestmentAmount(lastAmount);
                            lastAmount = lastAmount;
                        }
                    }
                    fundDetail.setExpiryDate(getAddMounth(fundCreateDto.getAccountDate(),monthSum(fundDetailDtoList,i)));
                }
                fundDetail.setInvestmentTerm(fundDetailDtoList.get(i).getInvestmentTerm());
                fundDetail.setReturnRate(fundDetailDtoList.get(i).getReturnRate());
                fundDetail.setExpiryProfit(fundDetailDtoList.get(i).getExpiryProfit());
                fundDetail.setContinueFlag(fundDetailDtoList.get(i).getContinueFlag());
                fundDetail.setContinueType(fundDetailDtoList.get(i).getContinueType());
                list.add(fundDetail);
            }
        }
        try {
            fundDetailDao.saveFundDetails(list);
        } catch (Exception e) {
            logger.error("createFundInfo save fundDetail has exception ,fundCreateDto is "+ JSONObject.toJSONString(fundCreateDto),e);
            throw e;
        }
        return responseData;
    }


    public Date getAddMounth(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }

    public int monthSum(List<FundDetailDto> fundDetailDtoList,int i) {
        int sum=0;
        if (fundDetailDtoList!=null&&fundDetailDtoList.size()>0) {
            for (int j = 0; j <= i; j++) {
                sum=sum+fundDetailDtoList.get(j).getInvestmentTerm();
            }
        }
        return sum;
    }


}
