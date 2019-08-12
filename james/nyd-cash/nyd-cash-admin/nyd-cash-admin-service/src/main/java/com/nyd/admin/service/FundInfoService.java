package com.nyd.admin.service;

import com.nyd.admin.model.dto.FundDetailQueryDto;
import com.tasfe.framework.support.model.ResponseData;

/**
 * @author Peng
 * @create 2018-01-02 14:43
 **/
public interface FundInfoService {
    ResponseData getFundInfo(FundDetailQueryDto dto,String accountNo) throws Exception;

    ResponseData getFundDetail(FundDetailQueryDto dto);
}
